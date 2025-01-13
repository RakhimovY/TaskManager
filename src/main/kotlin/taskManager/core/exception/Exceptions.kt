package taskManager.core.exception

class UserAlreadyExistException(errorMessage: String = "User already exist"): Exception(errorMessage)
class UserNotFounded(): Exception("User not founded")
