package tv.schnyder.aac.domain.model

// Type aliases for UIDs - these serialize cleanly as strings
typealias ElementUid = String
typealias ArchitectureUid = String
typealias LayerUid = String

// ArchiMate Relationship Types based on ArchiMate 3.1 specification
enum class RelationshipType {
    // Structural Relationships (in ascending order by strength)
    ASSOCIATION, // Weakest structural relationship - unspecified connection
    ASSIGNMENT, // Allocation of responsibility or performance of behavior
    REALIZATION, // Implementation or fulfillment of another element
    AGGREGATION, // Collection/member relationship
    COMPOSITION, // Strongest - whole/part relationship with existential dependency

    // Dynamic Relationships
    TRIGGERING, // Temporal or causal dependency between behavior elements
    FLOW, // Transfer of information, resources, or value

    // Dependency Relationships
    SERVING, // Provides functionality to another element
    ACCESS, // Behavioral elements accessing passive structure elements
    INFLUENCE, // One element affects another element

    // Other Relationships
    SPECIALIZATION, // Generalization/specialization relationship (is-a kind-of)
}

// Relationship categories for semantic grouping
enum class RelationshipCategory {
    STRUCTURAL, // Static construction or composition
    DYNAMIC, // Temporal dependencies
    DEPENDENCY, // Usage and support relationships
    OTHER, // Specialization and other relationships
}

// Flow types for FLOW relationships
enum class FlowType {
    INFORMATION, // Information flow
    VALUE, // Value flow (business value, money)
    GOODS, // Physical goods
    RESOURCES, // General resources
    DATA, // Data flow
    CONTROL, // Control flow
    EVENT, // Event flow
    SIGNAL, // Signal flow
    MATERIAL, // Material flow
}

// Access types for ACCESS relationships
enum class AccessType {
    READ, // Read access
    WRITE, // Write access
    READ_WRITE, // Read and write access
    ACCESS, // General access (default)
}

data class Relationship(
    val uid: ElementUid,
    val relationshipType: RelationshipType,
    val description: String? = null,
    val source: Any, // Can be any AnyElement from PKL
    val target: Any, // Can be any AnyElement from PKL
    val metadata: Map<String, String> = emptyMap(),
    val flowType: FlowType? = null, // Only for FLOW relationships
    val accessType: AccessType? = null, // Only for ACCESS relationships
) {
    // Computed property for relationship category
    val category: RelationshipCategory
        get() =
            when (relationshipType) {
                RelationshipType.ASSOCIATION,
                RelationshipType.ASSIGNMENT,
                RelationshipType.REALIZATION,
                RelationshipType.AGGREGATION,
                RelationshipType.COMPOSITION,
                -> RelationshipCategory.STRUCTURAL

                RelationshipType.TRIGGERING,
                RelationshipType.FLOW,
                -> RelationshipCategory.DYNAMIC

                RelationshipType.SERVING,
                RelationshipType.ACCESS,
                RelationshipType.INFLUENCE,
                -> RelationshipCategory.DEPENDENCY

                RelationshipType.SPECIALIZATION -> RelationshipCategory.OTHER
            }

    // Validation helper
    fun isValid(): Boolean =
        when (relationshipType) {
            RelationshipType.FLOW -> flowType != null
            RelationshipType.ACCESS -> accessType != null
            else -> true
        }

    // Get relationship strength (for structural relationships)
    val strength: Int
        get() =
            when (relationshipType) {
                RelationshipType.ASSOCIATION -> 1 // Weakest
                RelationshipType.ASSIGNMENT -> 2
                RelationshipType.REALIZATION -> 3
                RelationshipType.AGGREGATION -> 4
                RelationshipType.COMPOSITION -> 5 // Strongest
                else -> 0 // Non-structural relationships
            }
}

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
