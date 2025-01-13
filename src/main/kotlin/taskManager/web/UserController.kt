package taskManager.web

import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import taskManager.core.dto.UserDto
import taskManager.core.modles.User
import taskManager.core.services.UserService

@RestController
@RequestMapping("api/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUser(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<UserDto> = userService.getAllUsers(page, size)

    @GetMapping("find")
    fun getUserByEmail(
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) phoneNumber: String?
    ): ResponseEntity<List<UserDto>> = userService.findUserByParams(email, phoneNumber)

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserDto> = userService.getUserById(userId)

    @PutMapping("/{userId}")
    fun updateUserById(
        @PathVariable userId: Long,
        @RequestBody(required = true) userRequest: User
    ): ResponseEntity<UserDto> = userService.updateUserById(userId, userRequest)

    @DeleteMapping("/{userId}")
    fun deleteUserById(@PathVariable userId: Long): ResponseEntity<UserDto> = userService.deleteUserById(userId)

}