package tv.schnyder.aac.infrastructure.config

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tv.schnyder.aac.application.service.ArchitectureApplicationService

@Configuration
open class ModelLoadingStartup {
    private val logger = LoggerFactory.getLogger(ModelLoadingStartup::class.java)

    @Bean
    open fun loadArchitectureModelsOnStartup(architectureApplicationService: ArchitectureApplicationService) =
        ApplicationRunner {
            logger.info("Loading architecture models from PKL files on startup...")

            try {
                val loadedArchitectures = architectureApplicationService.reloadModelsFromFiles()
                logger.info("Successfully loaded ${loadedArchitectures.size} architecture model(s) on startup")

                loadedArchitectures.forEach { architecture ->
                    logger.info("Loaded architecture: ${architecture.name} (UID: ${architecture.uid})")
                }
            } catch (e: Exception) {
                logger.warn("Failed to load architecture models on startup: ${e.message}")
                logger.debug("Full exception:", e)
            }
        }
}
