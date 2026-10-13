package ru.webshop.backend.service.interfaces

import org.springframework.web.multipart.MultipartFile

interface PhotoStorageService {
     fun uploadPhoto(objectKey: String, content: ByteArray, contentType: String): String
     fun downloadPhoto(objectKey: String): ByteArray
     fun deletePhoto(objectKey: String)
}