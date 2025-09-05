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
@RequestMapping("/api/v1/architectures/{architectureId}/application")
@Tag(name = "Application Layer", description = "Application layer operations for PKL-loaded architecture models")
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

    @GetMapping("/applications")
    @Operation(
        summary = "Get applications",
        description = "Retrieve all applications from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved applications"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplications(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<Application>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.applicationLayer.applications)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/components")
    @Operation(
        summary = "Get application components",
        description = "Retrieve all application components from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application components"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationComponents(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<ApplicationComponent>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.applicationLayer.components)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/services")
    @Operation(
        summary = "Get application services",
        description = "Retrieve all application services from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application services"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationServices(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<ApplicationService>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.applicationLayer.services)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/interfaces")
    @Operation(
        summary = "Get application interfaces",
        description = "Retrieve all application interfaces from PKL models",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application interfaces"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationInterfaces(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
    ): ResponseEntity<List<ApplicationInterface>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(architecture.applicationLayer.interfaces)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/applications/{applicationId}")
    @Operation(
        summary = "Get application by ID",
        description = "Retrieve a specific application by its ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application"),
            ApiResponse(responseCode = "404", description = "Architecture or application not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationById(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Application ID") @PathVariable applicationId: String,
    ): ResponseEntity<Application> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val application =
                architecture.applicationLayer.applications.find { it.uid == applicationId }
                    ?: return ResponseEntity.notFound().build()

            ResponseEntity.ok(application)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/applications/by-stereotype/{stereotype}")
    @Operation(
        summary = "Get applications by stereotype",
        description = "Retrieve applications filtered by stereotype (BUSINESS_APPLICATION, IT_APPLICATION, etc.)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved applications"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid stereotype"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationsByStereotype(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Application Stereotype") @PathVariable stereotype: String,
    ): ResponseEntity<List<Application>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val stereoType =
                try {
                    val normalized = stereotype.replace("-", "_").uppercase()
                    ApplicationStereoType.valueOf(normalized)
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredApplications = architecture.applicationLayer.applications.filter { it.stereoType == stereoType }
            ResponseEntity.ok(filteredApplications)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/applications/by-lifecycle/{lifecycle}")
    @Operation(
        summary = "Get applications by lifecycle",
        description = "Retrieve applications filtered by lifecycle stage (PLAN, DEVELOP, ACTIVE, PHASEOUT, RETIRE)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved applications"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid lifecycle"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationsByLifecycle(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Application Lifecycle") @PathVariable lifecycle: String,
    ): ResponseEntity<List<Application>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val lifecycleStage =
                try {
                    ApplicationLifecycle.valueOf(lifecycle.uppercase())
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredApplications = architecture.applicationLayer.applications.filter { it.lifecycle == lifecycleStage }
            ResponseEntity.ok(filteredApplications)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/components/{componentId}")
    @Operation(
        summary = "Get application component by ID",
        description = "Retrieve a specific application component by its ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application component"),
            ApiResponse(responseCode = "404", description = "Architecture or component not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationComponentById(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Component ID") @PathVariable componentId: String,
    ): ResponseEntity<ApplicationComponent> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val component =
                architecture.applicationLayer.components.find { it.uid == componentId }
                    ?: return ResponseEntity.notFound().build()

            ResponseEntity.ok(component)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/components/by-type/{componentType}")
    @Operation(
        summary = "Get application components by type",
        description = "Retrieve application components filtered by type (FRONTEND, BACKEND, DATABASE, etc.)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application components"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid component type"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationComponentsByType(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Component Type") @PathVariable componentType: String,
    ): ResponseEntity<List<ApplicationComponent>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val type =
                try {
                    ApplicationComponentType.valueOf(componentType.uppercase())
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredComponents = architecture.applicationLayer.components.filter { it.componentType == type }
            ResponseEntity.ok(filteredComponents)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/interfaces/by-type/{interfaceType}")
    @Operation(
        summary = "Get application interfaces by type",
        description = "Retrieve application interfaces filtered by type (API, UI, FILE, MESSAGE)",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved application interfaces"),
            ApiResponse(responseCode = "404", description = "Architecture not found"),
            ApiResponse(responseCode = "400", description = "Invalid interface type"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ],
    )
    fun getApplicationInterfacesByType(
        @Parameter(description = "Architecture ID") @PathVariable architectureId: String,
        @Parameter(description = "Interface Type") @PathVariable interfaceType: String,
    ): ResponseEntity<List<ApplicationInterface>> {
        return try {
            val architecture =
                architectureApplicationService.getArchitectureById(architectureId)
                    ?: return ResponseEntity.notFound().build()

            val type =
                try {
                    ApplicationInterfaceType.valueOf(interfaceType.uppercase())
                } catch (e: IllegalArgumentException) {
                    return ResponseEntity.badRequest().build()
                }

            val filteredInterfaces = architecture.applicationLayer.interfaces.filter { it.interfaceType == type }
            ResponseEntity.ok(filteredInterfaces)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}
