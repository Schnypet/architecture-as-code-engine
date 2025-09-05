package tv.schnyder.aac.infrastructure.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tv.schnyder.aac.application.service.ArchitectureApplicationService
import tv.schnyder.aac.domain.model.BusinessActor
import tv.schnyder.aac.domain.model.BusinessCapability
import tv.schnyder.aac.domain.model.BusinessDomain
import tv.schnyder.aac.domain.model.BusinessLayer
import tv.schnyder.aac.domain.model.BusinessProcess
import tv.schnyder.aac.domain.model.BusinessService

@RestController
@RequestMapping("/api/v1/architectures/{architectureId}/business")
@Tag(name = "Business Layer", description = "Business layer operations for architecture models")
class BusinessLayerController(
    private val architectureApplicationService: ArchitectureApplicationService,
) {
    @GetMapping
    @Operation(
        summary = "Get complete business layer",
        description = "Retrieve the complete business layer for an architecture including all business elements",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business layer"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessLayer(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<BusinessLayer> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/domains")
    @Operation(
        summary = "Get business domains",
        description = "Retrieve all business domains from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business domains"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessDomains(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<BusinessDomain>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer.domains)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/capabilities")
    @Operation(
        summary = "Get business capabilities",
        description = "Retrieve all business capabilities from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business capabilities"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessCapabilities(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<BusinessCapability>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer.capabilities)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/actors")
    @Operation(
        summary = "Get business actors",
        description = "Retrieve all business actors from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business actors"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessActors(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<BusinessActor>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer.actors)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/processes")
    @Operation(
        summary = "Get business processes",
        description = "Retrieve all business processes from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business processes"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessProcesses(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<BusinessProcess>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer.processes)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/services")
    @Operation(
        summary = "Get business services",
        description = "Retrieve all business services from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business services"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessServices(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<BusinessService>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.businessLayer.services)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/actors/{actorId}")
    @Operation(
        summary = "Get business actor by ID",
        description = "Retrieve a specific business actor by its ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved business actor"),
            ApiResponse(responseCode = "404", description = "Architecture or actor not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getBusinessActorById(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Actor ID") @PathVariable actorId: String,
    ): ResponseEntity<BusinessActor> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val actor =
                architecture.businessLayer.actors.find { it.uid == actorId }
                    ?: return ResponseEntity.notFound().build()

            ResponseEntity.ok(actor)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}
