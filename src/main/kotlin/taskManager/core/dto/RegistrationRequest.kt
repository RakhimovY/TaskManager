package taskManager.core.dto

data class RegistrationRequest(
    val password: String,
    val phoneNumber: String,
    val email: String,
)