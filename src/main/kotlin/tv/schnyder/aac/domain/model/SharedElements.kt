package tv.schnyder.aac.domain.model

// Type aliases for UIDs - these serialize cleanly as strings
typealias ElementUid = String
typealias ArchitectureUid = String
typealias LayerUid = String

data class Relationship(
    val uid: ElementUid,
    val description: String? = null,
    val source: Any, // Can be any AnyElement from PKL
    val target: Any, // Can be any AnyElement from PKL
    val properties: Map<String, String> = emptyMap(),
)

enum class DataType {
    STRING,
    INTEGER,
    DECIMAL,
    BOOLEAN,
    DATE,
    DATETIME,
    JSON,
    XML,
    BINARY,
}
