package taskManager.core.dto

import taskManager.core.enum.TaskStatus

data class TaskDto(
    val id: Long,
    val title: String,
    val description: String,
    val status: TaskStatus
)