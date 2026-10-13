package ru.webshop.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Bean
    fun cookieSerializer(): DefaultCookieSerializer {
        return DefaultCookieSerializer().apply {
            setSameSite("None")
            setUseSecureCookie(true)
        }
    }
}