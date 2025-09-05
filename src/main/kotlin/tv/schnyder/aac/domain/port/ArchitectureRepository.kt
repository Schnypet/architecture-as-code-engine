package tv.schnyder.aac.domain.port

import tv.schnyder.aac.domain.model.Architecture
import tv.schnyder.aac.domain.model.ArchitectureUid

interface ArchitectureRepository {
    fun save(architecture: Architecture): Architecture

    fun findById(uid: ArchitectureUid): Architecture?

    fun findAll(): List<Architecture>

    fun delete(uid: ArchitectureUid): Boolean

    fun exists(uid: ArchitectureUid): Boolean
}
