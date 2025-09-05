package tv.schnyder.aac.infrastructure.adapter.pkl

import tv.schnyder.aac.domain.model.*
import org.slf4j.LoggerFactory

class PklToKotlinMapper {

    private val logger = LoggerFactory.getLogger(PklToKotlinMapper::class.java)

    fun mapToArchitecture(pklModel: Map<String, Any>): Architecture {
        logger.debug("Mapping PKL model to Architecture domain model")
        
        val objects = pklModel["objects"] as? List<Map<String, Any>> ?: emptyList()
        val relationships = pklModel["relationships"] as? List<Map<String, Any>> ?: emptyList()
        val moduleName = pklModel["_module"] as? String ?: "Unknown"
        
        return Architecture(
            uid = "arch-$moduleName",
            name = "$moduleName Architecture",
            description = "Architecture loaded from PKL module: $moduleName",
            version = "1.0.0",
            businessLayer = extractBusinessLayer(objects),
            applicationLayer = extractApplicationLayer(objects),
            technologyLayer = extractTechnologyLayer(objects),
            relationships = extractRelationships(relationships),
            metadata = mapOf("source" to (pklModel["_source"] ?: "unknown"))
        )
    }

    fun mergeModelsToArchitecture(
        businessModels: List<Map<String, Any>>,
        applicationModels: List<Map<String, Any>>,
        technologyModels: List<Map<String, Any>>
    ): Architecture {
        logger.debug("Merging PKL models to Architecture domain model")
        
        val allObjects = mutableListOf<Map<String, Any>>()
        val allRelationships = mutableListOf<Map<String, Any>>()
        val sourceFiles = mutableListOf<String>()
        
        // Collect all objects and relationships from all models
        listOf(businessModels, applicationModels, technologyModels).flatten().forEach { model ->
            val objects = model["objects"] as? List<Map<String, Any>> ?: emptyList()
            val relationships = model["relationships"] as? List<Map<String, Any>> ?: emptyList()
            
            allObjects.addAll(objects)
            allRelationships.addAll(relationships)
            
            model["_source"]?.let { sourceFiles.add(it.toString()) }
        }
        
        return Architecture(
            uid = "merged-architecture",
            name = "Merged Architecture",
            description = "Architecture merged from PKL models: ${sourceFiles.joinToString(", ")}",
            version = "1.0.0",
            businessLayer = extractBusinessLayer(allObjects),
            applicationLayer = extractApplicationLayer(allObjects),
            technologyLayer = extractTechnologyLayer(allObjects),
            relationships = extractRelationships(allRelationships),
            metadata = mapOf(
                "sources" to sourceFiles,
                "totalObjects" to allObjects.size,
                "totalRelationships" to allRelationships.size
            )
        )
    }

    private fun extractBusinessLayer(objects: List<Map<String, Any>>): BusinessLayer {
        val domains = mutableListOf<BusinessDomain>()
        val capabilities = mutableListOf<BusinessCapability>()
        val actors = mutableListOf<BusinessActor>()
        val processes = mutableListOf<BusinessProcess>()
        val services = mutableListOf<BusinessService>()
        
        objects.forEach { obj ->
            when (obj["_type"]) {
                "BusinessDomain" -> mapBusinessDomain(obj)?.let { domains.add(it) }
                "BusinessCapability" -> mapBusinessCapability(obj)?.let { capabilities.add(it) }
                "BusinessActor" -> mapBusinessActor(obj)?.let { actors.add(it) }
                "BusinessProcess" -> mapBusinessProcess(obj)?.let { processes.add(it) }
                "BusinessService" -> mapBusinessService(obj)?.let { services.add(it) }
            }
        }
        
        logger.debug("Extracted business layer: ${domains.size} domains, ${capabilities.size} capabilities, ${actors.size} actors, ${processes.size} processes, ${services.size} services")
        
        return BusinessLayer(
            uid = "business-layer",
            domains = domains,
            capabilities = capabilities,
            actors = actors,
            processes = processes,
            services = services
        )
    }

    private fun extractApplicationLayer(objects: List<Map<String, Any>>): ApplicationLayer {
        val applications = mutableListOf<Application>()
        val components = mutableListOf<ApplicationComponent>()
        val services = mutableListOf<ApplicationService>()
        val interfaces = mutableListOf<ApplicationInterface>()
        
        objects.forEach { obj ->
            when (obj["_type"]) {
                "Application" -> mapApplication(obj)?.let { applications.add(it) }
                "ApplicationComponent" -> mapApplicationComponent(obj)?.let { components.add(it) }
                "ApplicationService" -> mapApplicationService(obj)?.let { services.add(it) }
                "ApplicationInterface" -> mapApplicationInterface(obj)?.let { interfaces.add(it) }
            }
        }
        
        logger.debug("Extracted application layer: ${applications.size} applications, ${components.size} components, ${services.size} services, ${interfaces.size} interfaces")
        
        return ApplicationLayer(
            uid = "application-layer",
            applications = applications,
            components = components,
            services = services,
            interfaces = interfaces
        )
    }

    private fun extractTechnologyLayer(objects: List<Map<String, Any>>): TechnologyLayer {
        val nodes = mutableListOf<TechnologyNode>()
        val services = mutableListOf<TechnologyService>()
        val artifacts = mutableListOf<Artifact>()
        val interfaces = mutableListOf<TechnologyInterface>()
        val systemSoftware = mutableListOf<SystemSoftware>()
        
        objects.forEach { obj ->
            when (obj["_type"]) {
                "TechnologyNode" -> mapTechnologyNode(obj)?.let { nodes.add(it) }
                "TechnologyService" -> mapTechnologyService(obj)?.let { services.add(it) }
                "Artifact" -> mapArtifact(obj)?.let { artifacts.add(it) }
                "TechnologyInterface" -> mapTechnologyInterface(obj)?.let { interfaces.add(it) }
                "SystemSoftware" -> mapSystemSoftware(obj)?.let { systemSoftware.add(it) }
            }
        }
        
        logger.debug("Extracted technology layer: ${nodes.size} nodes, ${services.size} services, ${artifacts.size} artifacts, ${interfaces.size} interfaces, ${systemSoftware.size} system software")
        
        return TechnologyLayer(
            uid = "technology-layer",
            nodes = nodes,
            services = services,
            artifacts = artifacts,
            interfaces = interfaces,
            systemSoftware = systemSoftware
        )
    }

    private fun extractRelationships(relationshipObjects: List<Map<String, Any>>): List<Relationship> {
        return relationshipObjects.mapNotNull { obj ->
            mapRelationship(obj)
        }
    }

    // Business Layer Mappers
    private fun mapBusinessDomain(obj: Map<String, Any>): BusinessDomain? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        
        return BusinessDomain(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"])
        )
    }

    private fun mapBusinessCapability(obj: Map<String, Any>): BusinessCapability? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        
        return BusinessCapability(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            level = obj["level"] as? Int,
            parentCapability = (obj["parentCapability"] as? String)?.let { it }
        )
    }

    private fun mapBusinessActor(obj: Map<String, Any>): BusinessActor? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val actorTypeStr = obj["actorType"] as? String ?: "Internal"
        
        val actorType = try {
            ActorType.valueOf(actorTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ActorType.INTERNAL
        }
        
        return BusinessActor(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            actorType = actorType
        )
    }

    private fun mapBusinessProcess(obj: Map<String, Any>): BusinessProcess? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val processTypeStr = obj["processType"] as? String ?: "Core"
        
        val processType = try {
            ProcessType.valueOf(processTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ProcessType.CORE
        }
        
        return BusinessProcess(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            processType = processType,
            owner = (obj["owner"] as? String)?.let { it },
            inputs = extractStringList(obj["inputs"]),
            outputs = extractStringList(obj["outputs"])
        )
    }

    private fun mapBusinessService(obj: Map<String, Any>): BusinessService? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        
        return BusinessService(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            serviceLevel = obj["serviceLevel"] as? String,
            availability = obj["availability"] as? String
        )
    }

    // Application Layer Mappers
    private fun mapApplication(obj: Map<String, Any>): Application? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val stereoTypeStr = obj["stereoType"] as? String ?: "Business Application"
        val lifecycleStr = obj["lifecycle"] as? String ?: "Active"
        
        val stereoType = try {
            val normalized = stereoTypeStr.replace(" ", "_").uppercase()
            ApplicationStereoType.valueOf(normalized)
        } catch (e: IllegalArgumentException) {
            ApplicationStereoType.BUSINESS_APPLICATION
        }
        
        val lifecycle = try {
            ApplicationLifecycle.valueOf(lifecycleStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ApplicationLifecycle.ACTIVE
        }
        
        return Application(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            stereoType = stereoType,
            metadata = extractStringMap(obj["metadata"]),
            vendor = obj["vendor"] as? String,
            lifecycle = lifecycle
        )
    }

    private fun mapApplicationComponent(obj: Map<String, Any>): ApplicationComponent? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val componentTypeStr = obj["componentType"] as? String ?: "Backend"
        
        val componentType = try {
            ApplicationComponentType.valueOf(componentTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ApplicationComponentType.BACKEND
        }
        
        return ApplicationComponent(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            componentType = componentType,
            technology = obj["technology"] as? String
        )
    }

    private fun mapApplicationService(obj: Map<String, Any>): ApplicationService? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        
        return ApplicationService(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"])
        )
    }

    private fun mapApplicationInterface(obj: Map<String, Any>): ApplicationInterface? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val interfaceTypeStr = obj["interfaceType"] as? String ?: "API"
        
        val interfaceType = try {
            ApplicationInterfaceType.valueOf(interfaceTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ApplicationInterfaceType.API
        }
        
        return ApplicationInterface(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            interfaceType = interfaceType,
            format = obj["format"] as? String
        )
    }

    // Technology Layer Mappers
    private fun mapTechnologyNode(obj: Map<String, Any>): TechnologyNode? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val nodeTypeStr = obj["nodeType"] as? String ?: "Server"
        
        val nodeType = try {
            TechnologyNodeType.valueOf(nodeTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            TechnologyNodeType.SERVER
        }
        
        return TechnologyNode(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            nodeType = nodeType,
            location = obj["location"] as? String,
            capacity = obj["capacity"] as? String,
            operatingSystem = obj["operatingSystem"] as? String
        )
    }

    private fun mapTechnologyService(obj: Map<String, Any>): TechnologyService? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val serviceCategoryStr = obj["serviceCategory"] as? String ?: "Compute"
        
        val serviceCategory = try {
            TechnologyServiceCategory.valueOf(serviceCategoryStr.uppercase())
        } catch (e: IllegalArgumentException) {
            TechnologyServiceCategory.COMPUTE
        }
        
        return TechnologyService(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            serviceCategory = serviceCategory,
            provider = obj["provider"] as? String
        )
    }

    private fun mapArtifact(obj: Map<String, Any>): Artifact? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val artifactTypeStr = obj["artifactType"] as? String ?: "Configuration"
        
        val artifactType = try {
            ArtifactType.valueOf(artifactTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            ArtifactType.CONFIGURATION
        }
        
        return Artifact(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            artifactType = artifactType,
            format = obj["format"] as? String,
            size = obj["size"] as? String
        )
    }

    private fun mapTechnologyInterface(obj: Map<String, Any>): TechnologyInterface? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        
        return TechnologyInterface(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            protocol = obj["protocol"] as? String,
            port = obj["port"] as? Int
        )
    }

    private fun mapSystemSoftware(obj: Map<String, Any>): SystemSoftware? {
        val uid = obj["uid"] as? String ?: return null
        val name = obj["name"] as? String ?: ""
        val softwareTypeStr = obj["softwareType"] as? String ?: "Runtime"
        
        val softwareType = try {
            SystemSoftwareType.valueOf(softwareTypeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            SystemSoftwareType.RUNTIME
        }
        
        return SystemSoftware(
            uid = uid,
            name = name,
            description = obj["description"] as? String,
            documentation = obj["documentation"] as? String,
            properties = extractStringMap(obj["properties"]),
            softwareType = softwareType,
            vendor = obj["vendor"] as? String,
            version = obj["version"] as? String
        )
    }

    // Relationship Mapper
    private fun mapRelationship(obj: Map<String, Any>): Relationship? {
        val uid = obj["uid"] as? String ?: return null
        
        return Relationship(
            uid = uid,
            description = obj["description"] as? String,
            source = obj["source"] ?: "unknown",
            target = obj["target"] ?: "unknown",
            properties = extractStringMap(obj["properties"])
        )
    }

    // Helper methods
    private fun extractStringMap(value: Any?): Map<String, String> {
        return when (value) {
            is Map<*, *> -> {
                value.entries.mapNotNull { (k, v) ->
                    val key = k?.toString()
                    val val_ = v?.toString()
                    if (key != null && val_ != null) key to val_ else null
                }.toMap()
            }
            else -> emptyMap()
        }
    }

    private fun extractStringList(value: Any?): List<String> {
        return when (value) {
            is List<*> -> value.mapNotNull { it?.toString() }
            else -> emptyList()
        }
    }

    private fun extractElementUids(value: Any?): List<ElementUid> {
        return extractStringList(value).map { it }
    }
}