package tv.schnyder.aac.domain.model

// Technology Layer Elements matching PKL metamodel
data class TechnologyNode(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val nodeType: TechnologyNodeType,
    val location: String? = null,
    val capacity: String? = null,
    val operatingSystem: String? = null,
)

data class TechnologyService(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val serviceCategory: TechnologyServiceCategory,
    val provider: String? = null,
)

data class Artifact(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val artifactType: ArtifactType,
    val format: String? = null,
    val size: String? = null,
)

data class TechnologyInterface(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val protocol: String? = null,
    val port: Int? = null,
)

data class SystemSoftware(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val softwareType: SystemSoftwareType,
    val vendor: String? = null,
    val version: String? = null,
)

// Enums matching PKL metamodel
enum class TechnologyNodeType {
    SERVER,
    NETWORK,
    STORAGE,
    CLIENT,
    CLOUD,
}

enum class TechnologyServiceCategory {
    COMPUTE,
    STORAGE,
    NETWORK,
    SECURITY,
    MONITORING,
}

enum class ArtifactType {
    CONFIGURATION,
    DATA,
    SOFTWARE,
    PHYSICAL,
}

enum class SystemSoftwareType {
    OS,
    DATABASE,
    MIDDLEWARE,
    RUNTIME,
}
