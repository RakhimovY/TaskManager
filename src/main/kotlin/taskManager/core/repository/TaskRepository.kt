package taskManager.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import taskManager.core.modles.Task
import java.util.Optional

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUserId(userId: Long): List<Task>
    fun findByIdAndUserId(id: Long, userId: Long): Optional<Task>
}