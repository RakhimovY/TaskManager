package taskManager.core.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import taskManager.core.dto.RegistrationRequest
import taskManager.core.dto.UserDto
import taskManager.core.exception.UserAlreadyExistException
import taskManager.core.exception.UserNotFounded
import taskManager.core.modles.User
import taskManager.core.repository.UserRepository
import javax.transaction.Transactional

typealias ApplicationUserDetails = org.springframework.security.core.userdetails.User

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    fun getAllUsers(page: Int, size: Int): Page<UserDto> {
        val sort = Sort.by(Sort.Direction.ASC, "id")
        val pageable: Pageable = PageRequest.of(page, size, sort)
        val result = userRepository.getAllWithPagination(pageable).map {
            UserDto(
                it.id,
                it.email,
                it.phoneNumber,
                it.registrationDate.toString()
            )
        }
        return result
    }

    fun getUserByEmail(email: String): ResponseEntity<User> {
        val findUser = userRepository.getUserByEmail(email)
        if (findUser.isEmpty) {
            throw UserNotFounded()
        }
        return ResponseEntity.ok(findUser.get())
    }

    fun findUserByParams(email: String?, phoneNumber: String?): ResponseEntity<List<UserDto>> {
        val findUsersList = when {
            email != null && phoneNumber != null -> userRepository.getUsersByPhoneNumberAndEmail(phoneNumber, email)
            email != null -> listOf(userRepository.getUserByEmail(email).orElseThrow { UserNotFounded() })
            phoneNumber != null -> listOf(
                userRepository.getUserByPhoneNumber(phoneNumber).orElseThrow { UserNotFounded() })

            else -> throw IllegalArgumentException("Invalid parameters")
        }

        if (findUsersList.isEmpty()) {
            throw UserNotFounded()
        }
        return ResponseEntity.ok(findUsersList.map {
            UserDto(
                it.id,
                it.email,
                it.phoneNumber,
                it.registrationDate!!
            )
        })
    }

    fun creatNewUser(requestDto: RegistrationRequest): User {
        val findUserByEmail = userRepository.getUserByEmail(requestDto.email)
        val findUserByPhone = userRepository.getUserByPhoneNumber(requestDto.phoneNumber)

        if (findUserByEmail.isPresent || findUserByPhone.isPresent) {
            throw UserAlreadyExistException()
        }

        val user = User(
            0,
            requestDto.email,
            passwordEncoder.encode(requestDto.password),
            requestDto.phoneNumber,
        )

        return userRepository.save(user)
    }

    fun getUserById(userId: Long): ResponseEntity<UserDto> {
        val findUser = userRepository.getUserById(userId)
        if (findUser.isEmpty) {
            throw UserNotFounded()
        }
        return ResponseEntity.ok(
            UserDto(
                findUser.get().id,
                findUser.get().email,
                findUser.get().phoneNumber,
                findUser.get().registrationDate!!
            )
        )
    }

    @Transactional
    fun updateUserById(userId: Long, userRequest: User): ResponseEntity<UserDto> {
        val existingUser = userRepository.getUserById(userId).orElseThrow { UserNotFounded() }

        // Check if email is already used by another user
        userRepository.getUserByEmail(userRequest.email).ifPresent { user ->
            if (user.id != userId) {
                throw UserAlreadyExistException("Email is already used by another user")
            }
        }
        // Check if phone number is already used by another user
        userRepository.getUserByPhoneNumber(userRequest.phoneNumber).ifPresent { user ->
            if (user.id != userId) {
                throw UserAlreadyExistException("Phone number is already used by another user")
            }
        }

        existingUser.email = userRequest.email
        existingUser.phoneNumber = userRequest.phoneNumber
        existingUser.registrationDate = userRequest.registrationDate

        userRepository.save(existingUser)

        return ResponseEntity.ok(
            UserDto(
                existingUser.id,
                existingUser.email,
                existingUser.phoneNumber,
                existingUser.registrationDate!!
            )
        )
    }

    fun deleteUserById(userId: Long): ResponseEntity<UserDto> {
        val user = userRepository.getUserById(userId)
        if (user.isPresent) {
            userRepository.deleteById(userId)
            return ResponseEntity.ok(
                UserDto(
                    user.get().id,
                    user.get().email,
                    user.get().phoneNumber,
                    user.get().registrationDate!!
                )
            )
        } else throw UserNotFounded()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.getUserByEmail(username)
        return user.get().mapToUserDetails()
    }

    private fun User.mapToUserDetails(): UserDetails {
        return ApplicationUserDetails.builder()
            .username(this.email)
            .password(this.password)
            .roles("USER")
            .build()
    }
}
