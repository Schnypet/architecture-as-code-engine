package tv.schnyder.aac.infrastructure.adapter.repository

import org.springframework.stereotype.Repository
import tv.schnyder.aac.domain.model.Architecture
import tv.schnyder.aac.domain.model.ArchitectureUid
import tv.schnyder.aac.domain.port.ArchitectureRepository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryArchitectureRepository : ArchitectureRepository {
    private val architectures = ConcurrentHashMap<ArchitectureUid, Architecture>()

    override fun save(architecture: Architecture): Architecture {
        architectures[architecture.uid] = architecture
        return architecture
    }

    override fun findById(uid: ArchitectureUid): Architecture? = architectures[uid]

    override fun findAll(): List<Architecture> = architectures.values.toList()

    override fun delete(uid: ArchitectureUid): Boolean = architectures.remove(uid) != null

    override fun exists(uid: ArchitectureUid): Boolean = architectures.containsKey(uid)
}
