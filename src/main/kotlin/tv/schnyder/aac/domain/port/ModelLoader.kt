package tv.schnyder.aac.domain.port

import tv.schnyder.aac.domain.model.Architecture

interface ModelLoader {
    fun loadArchitectureModels(): List<Architecture>

    fun validateModel(architecture: Architecture): ValidationResult
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<ValidationError> = emptyList(),
    val warnings: List<ValidationWarning> = emptyList(),
)

data class ValidationError(
    val code: String,
    val message: String,
    val field: String? = null,
    val elementId: String? = null,
)

data class ValidationWarning(
    val code: String,
    val message: String,
    val field: String? = null,
    val elementId: String? = null,
)
