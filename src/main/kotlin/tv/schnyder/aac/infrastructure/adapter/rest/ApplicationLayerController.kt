package tv.schnyder.aac.infrastructure.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tv.schnyder.aac.application.service.ArchitectureApplicationService
import tv.schnyder.aac.domain.model.ApplicationLayer

@RestController
@RequestMapping("/api/v1/architectures/{architectureId}/application")
@Tag(name = "Application Layer", description = "Application layer operations for architecture models")
class ApplicationLayerController(
    private val architectureApplicationService: ArchitectureApplicationService,
) {
    @GetMapping
    @Operation(
        summary = "Get complete application layer",
        description = "Retrieve the complete application layer for an architecture including all application elements",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application layer"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationLayer(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<ApplicationLayer> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.applicationLayer)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}
