package taskManager.core.dto

data class UserDto(
    val id: Long,
    val email: String,
    val phoneNumber: String,
    val registrationDate: String
)