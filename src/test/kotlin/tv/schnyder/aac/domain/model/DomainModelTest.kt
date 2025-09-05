package tv.schnyder.aac.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DomainModelTest {
    @Test
    fun `BusinessActor should use uid instead of id`() {
        val businessActor =
            BusinessActor(
                uid = "BA01",
                name = "Kunde",
                description = "Endkunde",
                actorType = ActorType.EXTERNAL,
            )

        assertEquals("BA01", businessActor.uid)
        assertEquals("Kunde", businessActor.name)
        assertEquals(ActorType.EXTERNAL, businessActor.actorType)
    }

    @Test
    fun `Application should use uid instead of id`() {
        val application =
            Application(
                uid = "APP02",
                name = "Customer Portal",
                description = "Self-service portal",
                stereoType = ApplicationStereoType.BUSINESS_APPLICATION,
                lifecycle = ApplicationLifecycle.ACTIVE,
            )

        assertEquals("APP02", application.uid)
        assertEquals("Customer Portal", application.name)
        assertEquals(ApplicationStereoType.BUSINESS_APPLICATION, application.stereoType)
    }

    @Test
    fun `TechnologyNode should use uid instead of id`() {
        val techNode =
            TechnologyNode(
                uid = "TN01",
                name = "Application Server",
                description = "Primary server",
                nodeType = TechnologyNodeType.SERVER,
                location = "Data Center Zurich",
                capacity = "16 vCPU, 64 GB RAM",
            )

        assertEquals("TN01", techNode.uid)
        assertEquals("Application Server", techNode.name)
        assertEquals(TechnologyNodeType.SERVER, techNode.nodeType)
    }

    @Test
    fun `Architecture should use uid instead of id`() {
        val businessLayer =
            BusinessLayer(
                uid = "business-layer",
                actors =
                    listOf(
                        BusinessActor(
                            uid = "BA01",
                            name = "Customer",
                            actorType = ActorType.EXTERNAL,
                        ),
                    ),
            )

        val applicationLayer =
            ApplicationLayer(
                uid = "application-layer",
                applications =
                    listOf(
                        Application(
                            uid = "APP01",
                            name = "Customer App",
                            stereoType = ApplicationStereoType.BUSINESS_APPLICATION,
                            lifecycle = ApplicationLifecycle.ACTIVE,
                        ),
                    ),
            )

        val technologyLayer =
            TechnologyLayer(
                uid = "technology-layer",
                nodes =
                    listOf(
                        TechnologyNode(
                            uid = "TN01",
                            name = "Server",
                            nodeType = TechnologyNodeType.SERVER,
                        ),
                    ),
            )

        val architecture =
            Architecture(
                uid = "merged-architecture",
                name = "Test Architecture",
                description = "Test architecture with uid",
                version = "1.0.0",
                businessLayer = businessLayer,
                applicationLayer = applicationLayer,
                technologyLayer = technologyLayer,
            )

        assertEquals("merged-architecture", architecture.uid)
        assertEquals("business-layer", architecture.businessLayer.uid)
        assertEquals("application-layer", architecture.applicationLayer.uid)
        assertEquals("technology-layer", architecture.technologyLayer.uid)
    }

    @Test
    fun `Relationship should use uid instead of id`() {
        val relationship =
            Relationship(
                uid = "REL01",
                description = "Customer uses portal",
                source = "BA01",
                target = "APP01",
                properties = mapOf("type" to "uses"),
            )

        assertEquals("REL01", relationship.uid)
        assertEquals("Customer uses portal", relationship.description)
        assertEquals("BA01", relationship.source)
        assertEquals("APP01", relationship.target)
    }
}
