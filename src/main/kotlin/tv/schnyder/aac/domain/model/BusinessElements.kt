package tv.schnyder.aac.domain.model

// Business Domain Elements matching PKL metamodel
data class BusinessDomain(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val documentation: String? = null,
    val properties: Map<String, String> = emptyMap(),
    val stakeholders: List<ElementUid> = emptyList(),
)

data class BusinessCapability(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val documentation: String? = null,
    val properties: Map<String, String> = emptyMap(),
    val level: Int? = null,
    val parentCapability: ElementUid? = null,
)

data class BusinessActor(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val documentation: String? = null,
    val properties: Map<String, String> = emptyMap(),
    val actorType: ActorType,
)

data class BusinessProcess(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val documentation: String? = null,
    val properties: Map<String, String> = emptyMap(),
    val processType: ProcessType,
    val owner: ElementUid? = null,
    val inputs: List<String> = emptyList(),
    val outputs: List<String> = emptyList(),
)

data class BusinessService(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val documentation: String? = null,
    val properties: Map<String, String> = emptyMap(),
    val serviceLevel: String? = null,
    val availability: String? = null,
)

// Enums
enum class ActorType {
    INTERNAL,
    EXTERNAL,
    PARTNER,
}

enum class ProcessType {
    CORE,
    SUPPORT,
    MANAGEMENT,
}
