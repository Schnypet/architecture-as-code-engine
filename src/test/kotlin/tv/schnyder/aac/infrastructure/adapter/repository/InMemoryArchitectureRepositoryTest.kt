package tv.schnyder.aac.infrastructure.adapter.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import tv.schnyder.aac.domain.model.*

class InMemoryArchitectureRepositoryTest {

    private lateinit var repository: InMemoryArchitectureRepository

    @BeforeEach
    fun setUp() {
        repository = InMemoryArchitectureRepository()
    }

    @Test
    fun `should save and retrieve architecture by uid`() {
        val architecture = createTestArchitecture()
        
        val savedArchitecture = repository.save(architecture)
        assertEquals(architecture.uid, savedArchitecture.uid)
        
        val retrievedArchitecture = repository.findById(architecture.uid)
        assertNotNull(retrievedArchitecture)
        assertEquals(architecture.uid, retrievedArchitecture?.uid)
        assertEquals(architecture.name, retrievedArchitecture?.name)
    }

    @Test
    fun `should return null when architecture not found by uid`() {
        val nonExistentUid = "non-existent"
        val result = repository.findById(nonExistentUid)
        assertNull(result)
    }

    @Test
    fun `should return all saved architectures`() {
        val architecture1 = createTestArchitecture("arch1")
        val architecture2 = createTestArchitecture("arch2")
        
        repository.save(architecture1)
        repository.save(architecture2)
        
        val allArchitectures = repository.findAll()
        assertEquals(2, allArchitectures.size)
        assertTrue(allArchitectures.any { it.uid == architecture1.uid })
        assertTrue(allArchitectures.any { it.uid == architecture2.uid })
    }

    @Test
    fun `should delete architecture by uid`() {
        val architecture = createTestArchitecture()
        repository.save(architecture)
        
        val deleted = repository.delete(architecture.uid)
        assertTrue(deleted)
        
        val retrievedArchitecture = repository.findById(architecture.uid)
        assertNull(retrievedArchitecture)
    }

    @Test
    fun `should return false when deleting non-existent architecture`() {
        val nonExistentUid = "non-existent"
        val deleted = repository.delete(nonExistentUid)
        assertFalse(deleted)
    }

    @Test
    fun `should update existing architecture with same uid`() {
        val originalArchitecture = createTestArchitecture()
        repository.save(originalArchitecture)
        
        val updatedArchitecture = originalArchitecture.copy(name = "Updated Name")
        repository.save(updatedArchitecture)
        
        val retrievedArchitecture = repository.findById(originalArchitecture.uid)
        assertNotNull(retrievedArchitecture)
        assertEquals("Updated Name", retrievedArchitecture?.name)
        assertEquals(originalArchitecture.uid, retrievedArchitecture?.uid)
        
        // Should still only have one architecture
        val allArchitectures = repository.findAll()
        assertEquals(1, allArchitectures.size)
    }

    @Test
    fun `should only contain PKL data, no sample data`() {
        val allArchitectures = repository.findAll()
        
        // Repository should start empty
        assertTrue(allArchitectures.isEmpty(), "Repository should not contain any pre-loaded sample data")
    }

    private fun createTestArchitecture(uidSuffix: String = "test"): Architecture {
        return Architecture(
            uid = "architecture-$uidSuffix",
            name = "Test Architecture",
            description = "Test architecture for unit tests",
            version = "1.0.0",
            businessLayer = BusinessLayer(
                uid = "business-$uidSuffix",
                actors = listOf(
                    BusinessActor(
                        uid = "actor-$uidSuffix",
                        name = "Test Actor",
                        actorType = ActorType.INTERNAL
                    )
                )
            ),
            applicationLayer = ApplicationLayer(
                uid = "application-$uidSuffix",
                applications = listOf(
                    Application(
                        uid = "app-$uidSuffix",
                        name = "Test Application",
                        stereoType = ApplicationStereoType.BUSINESS_APPLICATION,
                        lifecycle = ApplicationLifecycle.ACTIVE
                    )
                )
            ),
            technologyLayer = TechnologyLayer(
                uid = "technology-$uidSuffix",
                nodes = listOf(
                    TechnologyNode(
                        uid = "node-$uidSuffix",
                        name = "Test Node",
                        nodeType = TechnologyNodeType.SERVER
                    )
                )
            )
        )
    }
}