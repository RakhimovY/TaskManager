package taskManager.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import taskManager.core.dto.LoginRequest
import taskManager.core.dto.LoginResponse
import taskManager.core.dto.RegistrationDto
import taskManager.core.dto.UserDto
import taskManager.core.services.AuthService

@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-up")
    fun registerNewUser(@RequestBody registrationDto: RegistrationDto): ResponseEntity<UserDto> {
        return authService.registerNewUser(registrationDto)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return authService.createAuthToken(loginRequest)
    }
}