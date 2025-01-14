package taskManager.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import taskManager.core.dto.TaskDto
import taskManager.core.dto.TaskRequest
import taskManager.core.dto.TaskUpdateRequest
import taskManager.core.services.TaskService
import java.security.Principal

@RestController
@RequestMapping("api/task")
class TaskController(
    private val taskService: TaskService,
) {

    @GetMapping()
    fun getTasksByUserId(principal: Principal): ResponseEntity<List<TaskDto>> {
        return taskService.findTasksByUserId(principal.name)
    }

    @PostMapping()
    fun addNewTask(@RequestBody taskRequest: TaskRequest, principal: Principal): ResponseEntity<TaskDto> {
        return taskService.addNewTask(taskRequest, principal.name)
    }

    @PatchMapping("/{taskId}")
    fun updateTaskStatus(
        @PathVariable taskId: Long,
        @RequestBody taskUpdateRequest: TaskUpdateRequest,
        principal: Principal
    ): ResponseEntity<TaskDto> {
        return taskService.updateTaskStatus(taskId, taskUpdateRequest, principal.name)
    }
}