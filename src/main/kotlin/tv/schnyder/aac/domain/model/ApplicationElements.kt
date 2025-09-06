package tv.schnyder.aac.domain.model

// Application Layer Elements matching PKL metamodel
data class Application(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val stereoType: ApplicationStereoType,
    val metadata: Map<String, String> = emptyMap(),
    val vendor: String? = null,
    val lifecycle: ApplicationLifecycle,
)

data class ApplicationComponent(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val componentType: ApplicationComponentType,
    val technology: String? = null,
)

data class ApplicationService(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)

data class ApplicationInterface(
    val uid: ElementUid,
    val name: String,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val interfaceType: ApplicationInterfaceType,
    val format: String? = null,
)

// Enums matching PKL metamodel
enum class ApplicationStereoType {
    BUSINESS_APPLICATION,
    IT_APPLICATION,
    PLATFORM,
    INFRASTRUCTURE,
    MICROSOLUTION,
}

enum class ApplicationLifecycle {
    PLAN,
    DEVELOP,
    ACTIVE,
    PHASEOUT,
    RETIRE,
}

enum class ApplicationComponentType {
    FRONTEND,
    BACKEND,
    DATABASE,
    INTEGRATION,
    ANALYTICS,
}

enum class ApplicationInterfaceType {
    API,
    UI,
    FILE,
    MESSAGE,
}
