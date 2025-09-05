package tv.schnyder.aac.domain.model

data class Architecture(
    val uid: ArchitectureUid,
    val name: String,
    val description: String,
    val version: String,
    val businessLayer: BusinessLayer,
    val applicationLayer: ApplicationLayer,
    val technologyLayer: TechnologyLayer,
    val relationships: List<Relationship> = emptyList(),
    val metadata: Map<String, Any> = emptyMap(),
)

data class BusinessLayer(
    val uid: LayerUid,
    val domains: List<BusinessDomain> = emptyList(),
    val capabilities: List<BusinessCapability> = emptyList(),
    val actors: List<BusinessActor> = emptyList(),
    val processes: List<BusinessProcess> = emptyList(),
    val services: List<BusinessService> = emptyList(),
)

data class ApplicationLayer(
    val uid: LayerUid,
    val applications: List<Application> = emptyList(),
    val components: List<ApplicationComponent> = emptyList(),
    val services: List<ApplicationService> = emptyList(),
    val interfaces: List<ApplicationInterface> = emptyList(),
)

data class TechnologyLayer(
    val uid: LayerUid,
    val nodes: List<TechnologyNode> = emptyList(),
    val services: List<TechnologyService> = emptyList(),
    val artifacts: List<Artifact> = emptyList(),
    val interfaces: List<TechnologyInterface> = emptyList(),
    val systemSoftware: List<SystemSoftware> = emptyList(),
)
