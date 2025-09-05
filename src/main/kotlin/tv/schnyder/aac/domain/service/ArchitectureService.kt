package tv.schnyder.aac.domain.service

import tv.schnyder.aac.domain.model.Application
import tv.schnyder.aac.domain.model.ApplicationService
import tv.schnyder.aac.domain.model.Architecture
import tv.schnyder.aac.domain.model.ArchitectureUid
import tv.schnyder.aac.domain.model.BusinessCapability
import tv.schnyder.aac.domain.model.ElementUid
import tv.schnyder.aac.domain.model.Relationship
import tv.schnyder.aac.domain.port.ArchitectureRepository
import tv.schnyder.aac.domain.port.ModelLoader
import tv.schnyder.aac.domain.port.ValidationResult

class ArchitectureService(
    private val architectureRepository: ArchitectureRepository,
    private val modelLoader: ModelLoader,
) {
    fun getAllArchitectures(): List<Architecture> = architectureRepository.findAll()

    fun getArchitecture(uid: ArchitectureUid): Architecture? = architectureRepository.findById(uid)

    fun saveArchitecture(architecture: Architecture): Architecture {
        val validationResult = modelLoader.validateModel(architecture)
        if (!validationResult.isValid) {
            throw ArchitectureValidationException("Architecture validation failed", validationResult.errors)
        }

        return architectureRepository.save(architecture)
    }

    fun deleteArchitecture(uid: ArchitectureUid): Boolean = architectureRepository.delete(uid)

    fun validateArchitecture(architecture: Architecture): ValidationResult = modelLoader.validateModel(architecture)

    fun findBusinessCapabilities(architectureUid: ArchitectureUid): List<BusinessCapability> {
        val architecture =
            architectureRepository.findById(architectureUid)
                ?: return emptyList()
        return architecture.businessLayer.capabilities
    }

    fun findApplications(architectureUid: ArchitectureUid): List<Application> {
        val architecture =
            architectureRepository.findById(architectureUid)
                ?: return emptyList()
        return architecture.applicationLayer.applications
    }

    fun findApplicationServices(architectureUid: ArchitectureUid): List<ApplicationService> {
        val architecture =
            architectureRepository.findById(architectureUid)
                ?: return emptyList()
        return architecture.applicationLayer.services
    }

    fun findRelationships(architectureUid: ArchitectureUid): List<Relationship> {
        val architecture =
            architectureRepository.findById(architectureUid)
                ?: return emptyList()
        return architecture.relationships
    }

    fun findRelationshipsForElement(
        architectureUid: ArchitectureUid,
        elementUid: ElementUid,
    ): List<Relationship> {
        val architecture =
            architectureRepository.findById(architectureUid)
                ?: return emptyList()

        // Note: New PKL relationship structure doesn't have simple sourceId/targetId
        // This would need to be implemented based on the actual relationship source/target objects
        return architecture.relationships.filter { relationship ->
            // For now, return empty until we implement proper element matching logic
            false
        }
    }

    fun reloadModelsFromFiles(): List<Architecture> {
        val architectures = modelLoader.loadArchitectureModels()

        // Save loaded architectures
        architectures.forEach { architecture ->
            try {
                architectureRepository.save(architecture)
            } catch (e: Exception) {
                // Log error but continue with other architectures
                throw ArchitectureLoadException("Failed to save loaded architecture: ${architecture.uid}", e)
            }
        }

        return architectures
    }
}

class ArchitectureValidationException(
    message: String,
    val errors: List<tv.schnyder.aac.domain.port.ValidationError>,
) : Exception(message)

class ArchitectureLoadException(
    message: String,
    cause: Throwable,
) : Exception(message, cause)
