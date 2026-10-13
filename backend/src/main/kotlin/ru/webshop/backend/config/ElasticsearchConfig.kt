package ru.webshop.backend.config
import org.springframework.context.annotation.*
import org.springframework.data.convert.Jsr310Converters
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions

@Configuration
class ElasticsearchConfig {
    @Bean
    fun elasticsearchCustomConversions(): ElasticsearchCustomConversions {
        return ElasticsearchCustomConversions(
            listOf(
                Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE,
                Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE
            )
        )
    }
}