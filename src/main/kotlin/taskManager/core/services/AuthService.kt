package taskManager.core.services

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import taskManager.core.dto.LoginRequest
import taskManager.core.dto.LoginResponse
import taskManager.core.dto.RegistrationRequest
import taskManager.core.dto.UserDto
import taskManager.core.exception.UserAlreadyExistException
import taskManager.core.repository.UserRepository
import taskManager.core.utils.JwtTokenUtils
import taskManager.core.utils.MessageUtil

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokenUtils,
    private val messageUtil: MessageUtil
) {
    fun registerNewUser(requestDto: RegistrationRequest?): ResponseEntity<UserDto> {
        if (requestDto == null){
             throw IllegalArgumentException("Request body is null")
        }
        val findUserByEmail = userRepository.getUserByEmail(requestDto.email)
        val findUserByPhone = userRepository.getUserByPhoneNumber(requestDto.phoneNumber)

        if (findUserByEmail.isPresent || findUserByPhone.isPresent) {
            throw UserAlreadyExistException()
        }

        val user = userService.creatNewUser(requestDto)

        return ResponseEntity.ok(UserDto(
            id = user.id,
            email = user.email,
            phoneNumber = user.phoneNumber,
            registrationDate = user.registrationDate!!
        ))
    }

    fun createAuthToken(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.email,
                    loginRequest.password
                )
            )
        } catch (_: BadCredentialsException) {
            throw RuntimeException(messageUtil.getMessage("error.user.not_found"))
        }
        val userDetails = userService.loadUserByUsername(loginRequest.email)
        val token = jwtTokenUtils.generateToken(userDetails)

        return ResponseEntity.ok(LoginResponse(token = token, privilege = userDetails.authorities))
    }
}
