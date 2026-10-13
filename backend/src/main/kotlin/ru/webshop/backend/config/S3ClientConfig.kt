package ru.webshop.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3ClientConfig(
    @Value("\${s3.region}")
    val region: String,
    @Value("\${s3.minioUrl}")
    val minioUrl: String,
    @Value("\${s3.accessKeyId}")
    val accessKeyId: String,
    @Value("\${s3.secretAccessKey}")
    val secretAccessKey: String
) {
    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)

        return S3Client.builder()
            .apply {
                if (minioUrl.isNotEmpty()) {
                    endpointOverride(URI.create(minioUrl))
                    forcePathStyle(true)
                }
            }
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(region))
            .build()
    }
}