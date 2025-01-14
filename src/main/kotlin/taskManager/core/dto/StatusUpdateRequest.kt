package taskManager.core.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import taskManager.core.enum.TaskStatus

data class TaskUpdateRequest @JsonCreator constructor(
    @JsonProperty("title") val title: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("status") val status: TaskStatus?
)