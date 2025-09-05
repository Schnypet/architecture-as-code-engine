package tv.schnyder.aac.infrastructure.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tv.schnyder.aac.application.service.ArchitectureApplicationService
import tv.schnyder.aac.domain.model.*
import tv.schnyder.aac.domain.port.ValidationResult

@RestController
@RequestMapping("/api/v1/architectures")
@Tag(name = "Architecture", description = "Architecture management operations")
class ArchitectureController(
    private val architectureApplicationService: ArchitectureApplicationService,
) {
    @GetMapping
    @Operation(summary = "Get all architectures", description = "Retrieve all architecture models")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved architectures"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getAllArchitectures(): ResponseEntity<List<Architecture>> =
        try {
            val architectures = architectureApplicationService.getAllArchitectures()
            ResponseEntity.ok(architectures)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @GetMapping("/{id}")
    @Operation(summary = "Get architecture by ID", description = "Retrieve a specific architecture by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved architecture"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getArchitectureById(
        @Parameter(description = "Architecture ID") @PathVariable id: String,
    ): ResponseEntity<Architecture> =
        try {
            val architecture = architectureApplicationService.getArchitectureById(id)
            architecture?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @PostMapping
    @Operation(summary = "Create architecture", description = "Create a new architecture model")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Successfully created architecture"),
            ApiResponse(responseCode = "400", description = "Invalid architecture data"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun createArchitecture(
        @Valid @RequestBody architecture: Architecture,
    ): ResponseEntity<Architecture> =
        try {
            val createdArchitecture = architectureApplicationService.createArchitecture(architecture)
            ResponseEntity.status(HttpStatus.CREATED).body(createdArchitecture)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }

    @PutMapping("/{id}")
    @Operation(summary = "Update architecture", description = "Update an existing architecture model")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated architecture"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid architecture data"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun updateArchitecture(
        @Parameter(description = "Architecture ID") @PathVariable id: String,
        @Valid @RequestBody architecture: Architecture,
    ): ResponseEntity<Architecture> {
        return try {
            val existingArchitecture = architectureApplicationService.getArchitectureById(id)
            if (existingArchitecture == null) {
                return ResponseEntity.notFound().build()
            }

            val updatedArchitecture = architecture.copy(uid = id)
            val result = architectureApplicationService.updateArchitecture(updatedArchitecture)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete architecture", description = "Delete an architecture model")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted architecture"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun deleteArchitecture(
        @Parameter(description = "Architecture ID") @PathVariable id: String,
    ): ResponseEntity<Void> =
        try {
            val deleted = architectureApplicationService.deleteArchitecture(id)
            if (deleted) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @PostMapping("/{id}/validate")
    @Operation(summary = "Validate architecture", description = "Validate an architecture model")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Validation completed"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun validateArchitecture(
        @Parameter(description = "Architecture ID") @PathVariable id: String,
    ): ResponseEntity<ValidationResult> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(id)
                    ?: return ResponseEntity.notFound().build()

            val validationResult = architectureApplicationService.validateArchitecture(architecture)
            ResponseEntity.ok(validationResult)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/reload")
    @Operation(summary = "Reload models from files", description = "Reload architecture models from PKL files")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Models reloaded successfully"),
            ApiResponse(responseCode = "500", description = "Error reloading models"),
        ],
    )
    fun reloadModelsFromFiles(): ResponseEntity<List<Architecture>> =
        try {
            val architectures = architectureApplicationService.reloadModelsFromFiles()
            ResponseEntity.ok(architectures)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
}
