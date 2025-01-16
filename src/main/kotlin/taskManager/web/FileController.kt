package taskManager.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import taskManager.core.services.FileService
import java.security.Principal

@RestController
@RequestMapping("api/file")
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile, principal: Principal
    ): ResponseEntity<Map<String, String>> {
        if (!file.contentType.equals("application/pdf", ignoreCase = true)) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Only PDF files are allowed"))
        }

        val fileName = fileService.uploadFile(file,principal.name)
        return ResponseEntity.ok(mapOf("fileName" to fileName))
    }

    @GetMapping("/download")
    fun downloadFile(principal: Principal): ResponseEntity<ByteArray> {
        val file = fileService.downloadFile(principal.name)
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=${file.second}")
            .body(file.first)
    }
}