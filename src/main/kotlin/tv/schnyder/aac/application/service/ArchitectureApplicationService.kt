package tv.schnyder.aac.application.service

import org.springframework.stereotype.Service
import tv.schnyder.aac.domain.model.*
import tv.schnyder.aac.domain.port.ValidationResult
import tv.schnyder.aac.domain.service.ArchitectureService

@Service
class ArchitectureApplicationService(
    private val architectureService: ArchitectureService,
) {
    fun getAllArchitectures(): List<Architecture> = architectureService.getAllArchitectures()

    fun getArchitectureById(id: String): Architecture? = architectureService.getArchitecture(id)

    fun createArchitecture(architecture: Architecture): Architecture = architectureService.saveArchitecture(architecture)

    fun updateArchitecture(architecture: Architecture): Architecture = architectureService.saveArchitecture(architecture)

    fun deleteArchitecture(id: String): Boolean = architectureService.deleteArchitecture(id)

    fun validateArchitecture(architecture: Architecture): ValidationResult = architectureService.validateArchitecture(architecture)

    fun getBusinessCapabilities(architectureId: String): List<BusinessCapability> =
        architectureService.findBusinessCapabilities(architectureId)

    fun getApplications(architectureId: String): List<Application> = architectureService.findApplications(architectureId)

    fun getApplicationServices(architectureId: String): List<ApplicationService> =
        architectureService.findApplicationServices(architectureId)


    fun getRelationships(architectureId: String): List<Relationship> =
        architectureService.findRelationships(architectureId)

    fun getRelationshipsForElement(
        architectureId: String,
        elementId: String,
    ): List<Relationship> =
        architectureService.findRelationshipsForElement(
            architectureId,
            elementId,
        )

    fun reloadModelsFromFiles(): List<Architecture> = architectureService.reloadModelsFromFiles()
}
