package ru.webshop.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring")
class PublicRoutersConfig {
    lateinit var publicUrls: List<String>
}