package tv.schnyder.aac.domain.service

import tv.schnyder.aac.domain.model.*
import tv.schnyder.aac.domain.port.ValidationError
import tv.schnyder.aac.domain.port.ValidationResult

/**
 * Service for validating ArchiMate relationships according to the metamodel specifications.
 * Provides semantic validation based on ArchiMate 3.1 relationship rules.
 */
class RelationshipValidationService {

    /**
     * Validates a single relationship according to ArchiMate rules
     */
    fun validateRelationship(relationship: Relationship): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        // Basic validation
        if (!relationship.isValid()) {
            when (relationship.relationshipType) {
                RelationshipType.FLOW -> {
                    errors.add(ValidationError(
                        code = "REL_001",
                        message = "FLOW relationship requires a flowType",
                        field = "flowType"
                    ))
                }
                RelationshipType.ACCESS -> {
                    errors.add(ValidationError(
                        code = "REL_002", 
                        message = "ACCESS relationship requires an accessType",
                        field = "accessType"
                    ))
                }
                else -> {}
            }
        }

        // Validate relationship type compatibility with source/target
        validateElementCompatibility(relationship, errors)

        // Validate flow type appropriateness
        validateFlowType(relationship, errors)

        // Validate access type appropriateness
        validateAccessType(relationship, errors)

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    /**
     * Validates multiple relationships for consistency and conflicts
     */
    fun validateRelationships(relationships: List<Relationship>): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        // Validate each individual relationship
        relationships.forEach { relationship ->
            val result = validateRelationship(relationship)
            errors.addAll(result.errors)
        }

        // Check for conflicting relationships
        validateRelationshipConflicts(relationships, errors)

        // Check for circular dependencies in structural relationships
        validateCircularDependencies(relationships, errors)

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    private fun validateElementCompatibility(relationship: Relationship, errors: MutableList<ValidationError>) {
        // This would need actual element type information to fully implement
        // For now, we'll add basic validation structure
        
        when (relationship.relationshipType) {
            RelationshipType.COMPOSITION, RelationshipType.AGGREGATION -> {
                // Structural relationships should connect compatible elements
                // Implementation would check element types from source/target
            }
            RelationshipType.TRIGGERING -> {
                // Should only connect behavioral elements (processes, functions, interactions, events)
            }
            RelationshipType.FLOW -> {
                // Should connect behavioral elements and optionally passive elements
            }
            RelationshipType.SERVING -> {
                // Source should provide service, target should consume it
            }
            RelationshipType.ACCESS -> {
                // Source should be behavioral element, target should be passive element
            }
            else -> {
                // Other relationships have more flexible rules
            }
        }
    }

    private fun validateFlowType(relationship: Relationship, errors: MutableList<ValidationError>) {
        if (relationship.relationshipType == RelationshipType.FLOW && relationship.flowType != null) {
            // Validate that flow type makes sense for the connected elements
            // This would need element type information to be fully implemented
            
            when (relationship.flowType) {
                FlowType.INFORMATION, FlowType.DATA -> {
                    // These are generally valid for most behavioral elements
                }
                FlowType.VALUE -> {
                    // Should typically connect business elements
                }
                FlowType.MATERIAL, FlowType.GOODS -> {
                    // Should typically connect physical elements
                }
                else -> {
                    // Other flow types are generally acceptable
                }
            }
        }
    }

    private fun validateAccessType(relationship: Relationship, errors: MutableList<ValidationError>) {
        if (relationship.relationshipType == RelationshipType.ACCESS && relationship.accessType != null) {
            // Access relationships should connect behavioral elements to passive elements
            // The access type should be appropriate for the target element
            
            when (relationship.accessType) {
                AccessType.READ -> {
                    // Read access is generally safe
                }
                AccessType.WRITE -> {
                    // Write access should be validated more strictly
                }
                AccessType.READ_WRITE -> {
                    // Should be used sparingly and with justification
                }
                AccessType.ACCESS -> {
                    // Generic access - acceptable but less specific
                }
            }
        }
    }

    private fun validateRelationshipConflicts(relationships: List<Relationship>, errors: MutableList<ValidationError>) {
        // Group relationships by source-target pairs
        val relationshipGroups = relationships.groupBy { "${it.source}-${it.target}" }

        relationshipGroups.forEach { (pair, rels) ->
            if (rels.size > 1) {
                // Check for conflicting relationship types
                val types = rels.map { it.relationshipType }.toSet()
                
                // Some relationships are mutually exclusive
                if (types.contains(RelationshipType.COMPOSITION) && types.contains(RelationshipType.AGGREGATION)) {
                    errors.add(ValidationError(
                        code = "REL_003",
                        message = "COMPOSITION and AGGREGATION relationships cannot exist between the same elements: $pair",
                        field = "relationshipType"
                    ))
                }
                
                // Multiple structural relationships of different strengths might indicate modeling issues
                val structuralTypes = types.filter { type ->
                    RelationshipCategory.STRUCTURAL == when (type) {
                        RelationshipType.ASSOCIATION,
                        RelationshipType.ASSIGNMENT,
                        RelationshipType.REALIZATION,
                        RelationshipType.AGGREGATION,
                        RelationshipType.COMPOSITION -> RelationshipCategory.STRUCTURAL
                        else -> RelationshipCategory.DEPENDENCY
                    }
                }
                
                if (structuralTypes.size > 1) {
                    errors.add(ValidationError(
                        code = "REL_004",
                        message = "Multiple structural relationships between same elements may indicate modeling issue: $pair",
                        field = "relationshipType"
                    ))
                }
            }
        }
    }

    private fun validateCircularDependencies(relationships: List<Relationship>, errors: MutableList<ValidationError>) {
        // Build dependency graph for structural relationships
        val dependencyMap = mutableMapOf<String, MutableSet<String>>()
        
        relationships.forEach { rel ->
            if (rel.category == RelationshipCategory.STRUCTURAL) {
                val sourceId = rel.source.toString()
                val targetId = rel.target.toString()
                dependencyMap.getOrPut(sourceId) { mutableSetOf() }.add(targetId)
            }
        }
        
        // Detect cycles using DFS
        val visited = mutableSetOf<String>()
        val recursionStack = mutableSetOf<String>()
        
        fun hasCycle(node: String): Boolean {
            if (recursionStack.contains(node)) return true
            if (visited.contains(node)) return false
            
            visited.add(node)
            recursionStack.add(node)
            
            dependencyMap[node]?.forEach { neighbor ->
                if (hasCycle(neighbor)) return true
            }
            
            recursionStack.remove(node)
            return false
        }
        
        dependencyMap.keys.forEach { node ->
            if (!visited.contains(node) && hasCycle(node)) {
                errors.add(ValidationError(
                    code = "REL_005",
                    message = "Circular dependency detected in structural relationships involving: $node",
                    field = "source"
                ))
            }
        }
    }

    /**
     * Get relationship compatibility information
     */
    fun getRelationshipInfo(relationshipType: RelationshipType): RelationshipInfo {
        return when (relationshipType) {
            RelationshipType.COMPOSITION -> RelationshipInfo(
                name = "Composition",
                description = "Represents a whole-part relationship with existential dependency",
                category = RelationshipCategory.STRUCTURAL,
                strength = 5,
                isDirectional = true,
                semantics = "The source element is composed of the target element. If the source is deleted, the target is also deleted."
            )
            RelationshipType.AGGREGATION -> RelationshipInfo(
                name = "Aggregation", 
                description = "Represents a collection-member relationship",
                category = RelationshipCategory.STRUCTURAL,
                strength = 4,
                isDirectional = true,
                semantics = "The source element aggregates the target element. The target can exist independently."
            )
            RelationshipType.REALIZATION -> RelationshipInfo(
                name = "Realization",
                description = "Shows implementation or fulfillment",
                category = RelationshipCategory.STRUCTURAL,
                strength = 3,
                isDirectional = true,
                semantics = "The source element realizes or implements the target element."
            )
            RelationshipType.ASSIGNMENT -> RelationshipInfo(
                name = "Assignment",
                description = "Allocation of responsibility or performance of behavior",
                category = RelationshipCategory.STRUCTURAL,
                strength = 2,
                isDirectional = true,
                semantics = "The source element is assigned to perform the target element."
            )
            RelationshipType.ASSOCIATION -> RelationshipInfo(
                name = "Association",
                description = "Generic unspecified relationship",
                category = RelationshipCategory.STRUCTURAL,
                strength = 1,
                isDirectional = false,
                semantics = "The source and target elements are associated in some way."
            )
            RelationshipType.TRIGGERING -> RelationshipInfo(
                name = "Triggering",
                description = "Temporal or causal dependency",
                category = RelationshipCategory.DYNAMIC,
                strength = 0,
                isDirectional = true,
                semantics = "The source element triggers the target element in time or causally."
            )
            RelationshipType.FLOW -> RelationshipInfo(
                name = "Flow",
                description = "Transfer of information, resources, or value",
                category = RelationshipCategory.DYNAMIC,
                strength = 0,
                isDirectional = true,
                semantics = "Something flows from the source element to the target element."
            )
            RelationshipType.SERVING -> RelationshipInfo(
                name = "Serving",
                description = "Provides functionality to another element",
                category = RelationshipCategory.DEPENDENCY,
                strength = 0,
                isDirectional = true,
                semantics = "The source element serves the target element by providing functionality."
            )
            RelationshipType.ACCESS -> RelationshipInfo(
                name = "Access",
                description = "Behavioral elements accessing passive elements",
                category = RelationshipCategory.DEPENDENCY,
                strength = 0,
                isDirectional = true,
                semantics = "The source element accesses the target element."
            )
            RelationshipType.INFLUENCE -> RelationshipInfo(
                name = "Influence",
                description = "One element affects another",
                category = RelationshipCategory.DEPENDENCY,
                strength = 0,
                isDirectional = true,
                semantics = "The source element influences the target element."
            )
            RelationshipType.SPECIALIZATION -> RelationshipInfo(
                name = "Specialization",
                description = "Generalization/specialization relationship",
                category = RelationshipCategory.OTHER,
                strength = 0,
                isDirectional = true,
                semantics = "The source element is a specialization of the target element."
            )
        }
    }
}

/**
 * Information about a relationship type
 */
data class RelationshipInfo(
    val name: String,
    val description: String,
    val category: RelationshipCategory,
    val strength: Int,
    val isDirectional: Boolean,
    val semantics: String
)