package taskManager.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import taskManager.core.modles.Task

interface TaskRepository : JpaRepository<Task, Long> {
}