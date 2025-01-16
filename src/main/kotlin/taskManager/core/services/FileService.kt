package taskManager.core.services

import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import taskManager.core.exception.UserNotFounded
import taskManager.core.modles.File
import taskManager.core.repository.FileRepository
import taskManager.core.repository.UserRepository
import javax.transaction.Transactional

@Service
class FileService(
    private val minioClient: MinioClient,
    private val userRepository: UserRepository,
    private val fileRepository: FileRepository,
    @Value("\${minio.bucket-name}") private val bucketName: String
) {

    @Transactional
    fun uploadFile(file: MultipartFile, email: String): String {
        val user = userRepository.getUserByEmail(email).orElseThrow { UserNotFounded() }
        val oldFile = fileRepository.findByFileUserId(user.id)

        try {
            val savedFile: File
            if (oldFile.isPresent) {
                oldFile.get().fileName = file.originalFilename!!
                savedFile = fileRepository.save(oldFile.get())
            } else {
                savedFile = fileRepository.save(
                    File(
                        fileName = file.originalFilename!!,
                        fileUser = user
                    )
                )
            }

            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`(savedFile.fileName)
                        .stream(file.inputStream, file.size, -1)
                        .contentType(file.contentType)
                        .build()
                )
            } catch (e: Exception) {
                throw RuntimeException("Ошибка загрузки в MinIO: ${e.message}", e)
            }
            return savedFile.fileName

        } catch (e: Exception) {
            throw RuntimeException("Не удалось выполнить операцию сохранения: ${e.message}", e)
        }
    }

    fun downloadFile(userEmail: String): Pair<ByteArray, String> {
        val user = userRepository.getUserByEmail(userEmail).orElseThrow { UserNotFounded() }
        val file = fileRepository.findByFileUserId(user.id).orElseThrow { Exception("Файл не найден") }
        val fileBiteArray = minioClient.getObject(
            io.minio.GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(file.fileName)
                .build()
        ).readAllBytes()
        return Pair(fileBiteArray, file.fileName)
    }
}
