package tv.schnyder.aac.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tv.schnyder.aac.domain.port.ArchitectureRepository
import tv.schnyder.aac.domain.port.ModelLoader
import tv.schnyder.aac.domain.service.ArchitectureService
import tv.schnyder.aac.domain.service.RelationshipValidationService

@Configuration
open class DomainConfiguration {
    @Bean
    open fun architectureService(
        architectureRepository: ArchitectureRepository,
        modelLoader: ModelLoader,
    ): ArchitectureService = ArchitectureService(architectureRepository, modelLoader)

    @Bean
    open fun relationshipValidationService(): RelationshipValidationService = RelationshipValidationService()
}
