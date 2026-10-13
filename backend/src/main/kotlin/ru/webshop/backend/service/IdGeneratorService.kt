package ru.webshop.backend.service

import org.springframework.stereotype.Service
import ru.webshop.backend.repository.ProductRepository
import java.util.concurrent.ThreadLocalRandom

@Service
class IdGeneratorService(
    private val productRepository: ProductRepository
) {
    fun generateArticleNumber(): Long {
        var articleNumber: Long
        do {
            articleNumber = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L)
        } while (productRepository.existsByArticleNumber(articleNumber))

        return articleNumber
    }
}