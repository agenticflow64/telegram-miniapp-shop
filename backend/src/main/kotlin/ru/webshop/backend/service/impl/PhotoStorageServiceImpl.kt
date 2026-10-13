package ru.webshop.backend.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.repository.PhotoRepository
import ru.webshop.backend.service.interfaces.PhotoStorageService
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest


@Service
class PhotoStorageServiceImpl(

    private val s3Client: S3Client,

    @Value("\${s3.bucket}")
    val bucketName: String,

    @Value("\${s3.minioUrl}")
    val minioUrl: String
    ) : PhotoStorageService {

    override  fun uploadPhoto(objectKey: String, content: ByteArray, contentType: String): String {

        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(content))

        val url = "$minioUrl/$bucketName/$objectKey"

        return url
    }

    override  fun downloadPhoto(objectKey: String): ByteArray  {
        val request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()

        return s3Client.getObject(request).use { response ->
            response.readAllBytes()
        }
    }

    override  fun deletePhoto(objectKey: String) {
        val request = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()

        s3Client.deleteObject(request)
    }
}
