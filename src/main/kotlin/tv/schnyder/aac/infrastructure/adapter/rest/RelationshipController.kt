package tv.schnyder.aac.infrastructure.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tv.schnyder.aac.application.service.ArchitectureApplicationService
import tv.schnyder.aac.domain.model.*
import tv.schnyder.aac.domain.service.RelationshipValidationService

@RestController
@RequestMapping("/api/v1/architectures/{architectureId}/relationships")
@Tag(name = "Relationships", description = "ArchiMate relationship operations")
class RelationshipController(
    private val architectureApplicationService: ArchitectureApplicationService,
    private val relationshipValidationService: RelationshipValidationService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get all relationships", 
        description = "Retrieve all relationships for an architecture"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved relationships"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getAllRelationships(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String
    ): ResponseEntity<List<Relationship>> =
        try {
            val relationships = architectureApplicationService.getRelationships(architectureId)
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(relationships)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @GetMapping("/by-type/{relationshipType}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get relationships by type",
        description = "Retrieve relationships filtered by ArchiMate relationship type"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved relationships"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid relationship type"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getRelationshipsByType(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Relationship type") @PathVariable relationshipType: String
    ): ResponseEntity<List<Relationship>> =
        try {
            val type = RelationshipType.valueOf(relationshipType.uppercase())
            val allRelationships = architectureApplicationService.getRelationships(architectureId)
            val filteredRelationships = allRelationships.filter { it.relationshipType == type }
            
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filteredRelationships)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @GetMapping("/by-category/{category}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get relationships by category",
        description = "Retrieve relationships filtered by ArchiMate relationship category (STRUCTURAL, DYNAMIC, DEPENDENCY, OTHER)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved relationships"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid relationship category"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getRelationshipsByCategory(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Relationship category") @PathVariable category: String
    ): ResponseEntity<List<Relationship>> =
        try {
            val categoryEnum = RelationshipCategory.valueOf(category.uppercase())
            val allRelationships = architectureApplicationService.getRelationships(architectureId)
            val filteredRelationships = allRelationships.filter { it.category == categoryEnum }
            
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filteredRelationships)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @GetMapping("/for-element/{elementId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get relationships for element",
        description = "Retrieve all relationships where the specified element is source or target"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved relationships"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getRelationshipsForElement(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Element ID") @PathVariable elementId: String
    ): ResponseEntity<List<Relationship>> =
        try {
            val relationships = architectureApplicationService.getRelationshipsForElement(architectureId, elementId)
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(relationships)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }


    @GetMapping("/structural", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get structural relationships ordered by strength",
        description = "Retrieve structural relationships ordered from weakest (ASSOCIATION) to strongest (COMPOSITION)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved structural relationships"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getStructuralRelationships(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String
    ): ResponseEntity<List<Relationship>> =
        try {
            val allRelationships = architectureApplicationService.getRelationships(architectureId)
            val structuralRelationships = allRelationships
                .filter { it.category == RelationshipCategory.STRUCTURAL }
                .sortedBy { it.strength }
            
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(structuralRelationships)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }

    @PostMapping("/validate", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Validate relationships",
        description = "Validate all relationships in an architecture for ArchiMate compliance"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Validation completed"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun validateRelationships(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String
    ): ResponseEntity<Map<String, Any>> =
        try {
            val relationships = architectureApplicationService.getRelationships(architectureId)
            val validationResult = relationshipValidationService.validateRelationships(relationships)
            
            val response = mapOf(
                "isValid" to validationResult.isValid,
                "errors" to validationResult.errors,
                "relationshipCount" to relationships.size,
                "validationSummary" to mapOf(
                    "structural" to relationships.count { it.category == RelationshipCategory.STRUCTURAL },
                    "dynamic" to relationships.count { it.category == RelationshipCategory.DYNAMIC },
                    "dependency" to relationships.count { it.category == RelationshipCategory.DEPENDENCY },
                    "other" to relationships.count { it.category == RelationshipCategory.OTHER }
                )
            )
            
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
}