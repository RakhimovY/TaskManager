package taskManager.core.services

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import taskManager.core.dto.TaskDto
import taskManager.core.dto.TaskRequest
import taskManager.core.dto.TaskUpdateRequest
import taskManager.core.exception.TasksNotFounded
import taskManager.core.exception.UserNotFounded
import taskManager.core.modles.Task
import taskManager.core.repository.TaskRepository
import taskManager.core.repository.UserRepository
import javax.transaction.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) {

    fun findTasksByUserId(email: String): ResponseEntity<List<TaskDto>> {
        val user = userRepository.getUserByEmail(email).orElseThrow { UserNotFounded() }

        val tasks = taskRepository.findByUserId(user.id)
        if (tasks.isEmpty()) throw TasksNotFounded()

        val taskDtoList = tasks.map { TaskDto(it.id, it.title, it.description, it.status!!) }
        return ResponseEntity.ok(taskDtoList)
    }

    fun addNewTask(taskRequest: TaskRequest, email: String): ResponseEntity<TaskDto> {
        val user = userRepository.getUserByEmail(email).orElseThrow { UserNotFounded() }
        val task = Task(
            title = taskRequest.title,
            description = taskRequest.description,
            user = user
        )
        taskRepository.save(task)
        return ResponseEntity.ok(
            TaskDto(
                task.id,
                task.title,
                task.description,
                task.status!!
            )
        )
    }

    @Transactional
    fun updateTaskStatus(taskId: Long, taskUpdateRequest: TaskUpdateRequest, email: String): ResponseEntity<TaskDto> {
        val user = userRepository.getUserByEmail(email).orElseThrow { UserNotFounded() }
        val task = taskRepository.findByIdAndUserId(taskId, user.id).orElseThrow { TasksNotFounded() }
        task.apply {
            title = taskUpdateRequest.title ?: title
            description = taskUpdateRequest.description ?: description
            status = taskUpdateRequest.status ?: status
        }

        taskRepository.save(task)
        return ResponseEntity.ok(
            TaskDto(
                task.id,
                task.title,
                task.description,
                task.status!!
            )
        )
    }
}