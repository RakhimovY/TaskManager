package taskManager.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import taskManager.core.modles.File
import java.util.Optional

interface FileRepository : JpaRepository<File, Long> {
    fun findByFileUserId(userId: Long): Optional<File>
}