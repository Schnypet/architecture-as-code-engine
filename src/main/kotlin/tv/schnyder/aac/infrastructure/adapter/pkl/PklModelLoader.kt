package tv.schnyder.aac.infrastructure.adapter.pkl

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import tv.schnyder.aac.domain.model.Architecture
import tv.schnyder.aac.domain.port.ModelLoader
import tv.schnyder.aac.domain.port.ValidationError
import tv.schnyder.aac.domain.port.ValidationResult
import java.io.BufferedReader
import java.util.regex.Pattern

@Service
@Primary
class PklModelLoader : ModelLoader {
    private val logger = LoggerFactory.getLogger(PklModelLoader::class.java)
    private val resourcePatternResolver = PathMatchingResourcePatternResolver()
    private val mapper = PklToKotlinMapper()

    @PostConstruct
    fun initialize() {
        logger.info("Initializing PKL Model Loader with direct file parsing")
    }

    @PreDestroy
    fun cleanup() {
        logger.info("PKL Model Loader cleaned up")
    }

    override fun loadArchitectureModels(): List<Architecture> {
        logger.info("Loading all architecture models from PKL files in models directory")

        return try {
            val businessModels = loadModelsFromDirectory("classpath:models/business/*.pkl")
            val applicationModels = loadModelsFromDirectory("classpath:models/application/*.pkl")
            val technologyModels = loadModelsFromDirectory("classpath:models/technology/*.pkl")

            logger.info(
                "Found ${businessModels.size} business models, ${applicationModels.size} application models, ${technologyModels.size} technology models",
            )

            // Create merged architecture from all models
            val architecture = mapper.mergeModelsToArchitecture(businessModels, applicationModels, technologyModels)
            listOf(architecture)
        } catch (e: Exception) {
            logger.error("Failed to load architecture models", e)
            // Return empty list instead of sample to avoid confusion
            emptyList()
        }
    }

    override fun loadArchitectureModel(path: String): Architecture? {
        logger.info("Loading architecture model from: $path")

        return try {
            val resource = resourcePatternResolver.getResource(path)
            if (!resource.exists()) {
                logger.warn("PKL model file not found: $path")
                return null
            }

            val pklData = parsePklFile(resource)
            mapper.mapToArchitecture(pklData)
        } catch (e: Exception) {
            logger.error("Failed to load architecture model from: $path", e)
            null
        }
    }

    override fun validateModel(architecture: Architecture): ValidationResult {
        logger.debug("Validating architecture model: ${architecture.uid}")

        val errors = mutableListOf<ValidationError>()

        // Basic validation
        if (architecture.name.isBlank()) {
            errors.add(ValidationError("ARCH_001", "Architecture name cannot be blank", "name"))
        }

        if (architecture.version.isBlank()) {
            errors.add(ValidationError("ARCH_002", "Architecture version cannot be blank", "version"))
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
        )
    }

    private fun loadModelsFromDirectory(pattern: String): List<Map<String, Any>> =
        try {
            val resources: Array<Resource> = resourcePatternResolver.getResources(pattern)
            logger.info("Found ${resources.size} PKL files matching pattern: $pattern")

            resources.mapNotNull { resource ->
                try {
                    logger.debug("Loading PKL file: ${resource.filename}")
                    parsePklFile(resource)
                } catch (e: Exception) {
                    logger.error("Failed to load PKL file: ${resource.filename}", e)
                    null
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to load models from directory: $pattern", e)
            emptyList()
        }

    private fun parsePklFile(resource: Resource): Map<String, Any> {
        val pklContent = resource.inputStream.bufferedReader().use(BufferedReader::readText)
        logger.debug("Parsing PKL content from: ${resource.filename}")

        return parsePklContent(pklContent, resource.filename ?: "unknown")
    }

    private fun parsePklContent(
        content: String,
        filename: String,
    ): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        result["_source"] = filename

        // Parse module name
        val modulePattern = Pattern.compile("module\\s+(\\w+)")
        val moduleMatcher = modulePattern.matcher(content)
        if (moduleMatcher.find()) {
            result["_module"] = moduleMatcher.group(1)
        }

        // Parse simple object declarations (local declarations)
        val objectPattern = Pattern.compile("local\\s+(\\w+):\\s*(\\w+)\\s*=\\s*new\\s*\\{([^}]+)\\}")
        val objectMatcher = objectPattern.matcher(content)
        val objects = mutableListOf<Map<String, Any>>()

        while (objectMatcher.find()) {
            val objectName = objectMatcher.group(1)
            val objectType = objectMatcher.group(2)
            val objectBody = objectMatcher.group(3)

            val objectData = parseObjectBody(objectBody)
            objectData["_name"] = objectName
            objectData["_type"] = objectType

            objects.add(objectData)
        }

        // Parse top-level object declarations (without local keyword)
        val topLevelPattern = Pattern.compile("(\\w+):\\s*(\\w+)\\s*=\\s*new\\s*\\{([^}]+)\\}")
        val topLevelMatcher = topLevelPattern.matcher(content)

        while (topLevelMatcher.find()) {
            val objectName = topLevelMatcher.group(1)
            val objectType = topLevelMatcher.group(2)
            val objectBody = topLevelMatcher.group(3)

            val objectData = parseObjectBody(objectBody)
            objectData["_name"] = objectName
            objectData["_type"] = objectType

            objects.add(objectData)
        }

        result["objects"] = objects

        // Parse relationships
        val relationships = mutableListOf<Map<String, Any>>()
        objects.forEach { obj ->
            if (obj["_type"] == "Relationship") {
                relationships.add(obj)
            }
        }
        result["relationships"] = relationships

        logger.debug("Parsed ${objects.size} objects and ${relationships.size} relationships from $filename")

        return result
    }

    private fun parseObjectBody(body: String): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()

        // Parse key-value pairs
        val lines = body.split("\n")
        lines.forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("//")) {
                // Handle simple key = value pairs
                val keyValuePattern = Pattern.compile("(\\w+)\\s*=\\s*(.+)")
                val matcher = keyValuePattern.matcher(trimmedLine)
                if (matcher.find()) {
                    val key = matcher.group(1)
                    var value = matcher.group(2).trim()

                    // Remove trailing comma if present
                    if (value.endsWith(",")) {
                        value = value.substring(0, value.length - 1)
                    }

                    // Parse different value types
                    result[key] = parseValue(value)
                }
            }
        }

        return result
    }

    private fun parseValue(value: String): Any =
        when {
            value.startsWith("\"") && value.endsWith("\"") -> {
                // String value
                value.substring(1, value.length - 1)
            }
            value == "true" || value == "false" -> {
                // Boolean value
                value.toBoolean()
            }
            value.matches(Regex("\\d+")) -> {
                // Integer value
                value.toInt()
            }
            value.matches(Regex("\\d*\\.\\d+")) -> {
                // Double value
                value.toDouble()
            }
            value.startsWith("Map(") -> {
                // Map value - simplified parsing
                parseMapValue(value)
            }
            value.contains(".") && !value.startsWith("\"") -> {
                // Reference to another object (like BA.customer)
                value
            }
            else -> {
                // Default to string
                value
            }
        }

    private fun parseMapValue(mapValue: String): Map<String, String> {
        val result = mutableMapOf<String, String>()

        // Simple map parsing - Map("key", "value", "key2", "value2")
        val mapContent = mapValue.substring(4, mapValue.length - 1) // Remove Map( and )
        val parts = mapContent.split(",").map { it.trim() }

        var i = 0
        while (i < parts.size - 1) {
            val key = parts[i].trim('"')
            val value = parts[i + 1].trim('"')
            result[key] = value
            i += 2
        }

        return result
    }
}
