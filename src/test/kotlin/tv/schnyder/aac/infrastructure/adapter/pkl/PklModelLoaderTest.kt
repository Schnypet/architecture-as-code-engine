package tv.schnyder.aac.infrastructure.adapter.pkl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tv.schnyder.aac.domain.model.*

class PklModelLoaderTest {
    private lateinit var pklModelLoader: PklModelLoader

    @BeforeEach
    fun setUp() {
        pklModelLoader = PklModelLoader()
    }

    @Test
    fun `should load architecture models from PKL files with uid fields`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty(), "Should load at least one architecture from PKL files")

        val architecture = architectures.first()
        assertNotNull(architecture.uid)
        assertFalse(architecture.uid.isBlank(), "Architecture uid should not be blank")
    }

    @Test
    fun `loaded business actors should have uid from PKL files`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty())

        val architecture = architectures.first()
        val businessActors = architecture.businessLayer.actors

        if (businessActors.isNotEmpty()) {
            val actor = businessActors.first()
            assertNotNull(actor.uid)
            assertFalse(actor.uid.isBlank(), "BusinessActor uid should not be blank")
            assertNotNull(actor.name)
            assertNotNull(actor.actorType)
        }
    }

    @Test
    fun `loaded applications should have uid from PKL files`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty())

        val architecture = architectures.first()
        val applications = architecture.applicationLayer.applications

        if (applications.isNotEmpty()) {
            val application = applications.first()
            assertNotNull(application.uid)
            assertFalse(application.uid.isBlank(), "Application uid should not be blank")
            assertNotNull(application.name)
            assertNotNull(application.stereoType)
            assertNotNull(application.lifecycle)
        }
    }

    @Test
    fun `loaded technology nodes should have uid from PKL files`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty())

        val architecture = architectures.first()
        val techNodes = architecture.technologyLayer.nodes

        if (techNodes.isNotEmpty()) {
            val node = techNodes.first()
            assertNotNull(node.uid)
            assertFalse(node.uid.isBlank(), "TechnologyNode uid should not be blank")
            assertNotNull(node.name)
            assertNotNull(node.nodeType)
        }
    }

    @Test
    fun `should validate architecture models with uid`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty())

        val architecture = architectures.first()
        val validationResult = pklModelLoader.validateModel(architecture)

        assertNotNull(validationResult)
        assertTrue(validationResult.isValid, "Loaded architecture should be valid")
    }

    @Test
    fun `should not contain any sample or dummy data`() {
        val architectures = pklModelLoader.loadArchitectureModels()

        assertNotNull(architectures)
        assertTrue(architectures.isNotEmpty())

        val architecture = architectures.first()

        // Verify no sample data patterns
        assertFalse(architecture.name.contains("Sample"), "Should not contain sample data")
        assertFalse(architecture.description?.contains("sample") == true, "Should not contain sample descriptions")
        assertFalse(architecture.uid.contains("sample"), "Should not contain sample uid")

        // Verify business actors don't contain sample data
        architecture.businessLayer.actors.forEach { actor ->
            assertFalse(actor.name.contains("Sample"), "Business actor should not contain sample data")
            assertFalse(actor.uid.contains("sample"), "Business actor uid should not contain sample data")
        }

        // Verify applications don't contain sample data
        architecture.applicationLayer.applications.forEach { app ->
            assertFalse(app.name.contains("Sample"), "Application should not contain sample data")
            assertFalse(app.uid.contains("sample"), "Application uid should not contain sample data")
        }

        // Verify technology nodes don't contain sample data
        architecture.technologyLayer.nodes.forEach { node ->
            assertFalse(node.name.contains("Sample"), "Technology node should not contain sample data")
            assertFalse(node.uid.contains("sample"), "Technology node uid should not contain sample data")
        }
    }
}
