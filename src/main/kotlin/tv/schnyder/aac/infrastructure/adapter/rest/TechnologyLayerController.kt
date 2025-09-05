package tv.schnyder.aac.infrastructure.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tv.schnyder.aac.application.service.ArchitectureApplicationService
import tv.schnyder.aac.domain.model.*

@RestController
@RequestMapping("/api/v1/architectures/{architectureId}/technology")
@Tag(name = "Technology Layer", description = "Technology layer operations for PKL-loaded architecture models")
class TechnologyLayerController(
    private val architectureApplicationService: ArchitectureApplicationService,
) {
    @GetMapping
    @Operation(
        summary = "Get complete technology layer",
        description = "Retrieve the complete technology layer for an architecture including all technology elements",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology layer"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyLayer(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<TechnologyLayer> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/nodes")
    @Operation(
        summary = "Get technology nodes",
        description = "Retrieve all technology nodes from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology nodes"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyNodes(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<TechnologyNode>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer.nodes)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/services")
    @Operation(
        summary = "Get technology services",
        description = "Retrieve all technology services from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology services"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyServices(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<TechnologyService>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer.services)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/artifacts")
    @Operation(
        summary = "Get artifacts",
        description = "Retrieve all artifacts from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved artifacts"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getArtifacts(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<Artifact>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer.artifacts)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/interfaces")
    @Operation(
        summary = "Get technology interfaces",
        description = "Retrieve all technology interfaces from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology interfaces"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyInterfaces(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<TechnologyInterface>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer.interfaces)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/system-software")
    @Operation(
        summary = "Get system software",
        description = "Retrieve all system software from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved system software"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getSystemSoftware(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<SystemSoftware>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.technologyLayer.systemSoftware)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/nodes/{nodeId}")
    @Operation(
        summary = "Get technology node by ID",
        description = "Retrieve a specific technology node by its ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology node"),
            ApiResponse(responseCode = "404", description = "Architecture or node not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyNodeById(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Node ID") @PathVariable nodeId: String,
    ): ResponseEntity<TechnologyNode> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val node =
                architecture.technologyLayer.nodes.find { it.uid == nodeId }
                    ?: return ResponseEntity.notFound().build()

            ResponseEntity.ok(node)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/nodes/by-type/{nodeType}")
    @Operation(
        summary = "Get technology nodes by type",
        description = "Retrieve technology nodes filtered by type (SERVER, NETWORK, STORAGE, CLIENT, CLOUD)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology nodes"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid node type"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyNodesByType(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Node Type") @PathVariable nodeType: String,
    ): ResponseEntity<List<TechnologyNode>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val type =
                try {
                    TechnologyNodeType.valueOf(nodeType.uppercase())
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredNodes = architecture.technologyLayer.nodes.filter { it.nodeType == type }
            ResponseEntity.ok(filteredNodes)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/services/by-category/{serviceCategory}")
    @Operation(
        summary = "Get technology services by category",
        description = "Retrieve technology services filtered by category (COMPUTE, STORAGE, NETWORK, SECURITY, MONITORING)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved technology services"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid service category"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getTechnologyServicesByCategory(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Service Category") @PathVariable serviceCategory: String,
    ): ResponseEntity<List<TechnologyService>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val category =
                try {
                    TechnologyServiceCategory.valueOf(serviceCategory.uppercase())
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredServices = architecture.technologyLayer.services.filter { it.serviceCategory == category }
            ResponseEntity.ok(filteredServices)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

}
