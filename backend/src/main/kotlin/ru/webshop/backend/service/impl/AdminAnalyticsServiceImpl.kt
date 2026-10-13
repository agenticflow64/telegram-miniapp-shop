package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.webshop.backend.dto.admin.*
import ru.webshop.backend.enums.OrderStatus
import ru.webshop.backend.repository.*
import ru.webshop.backend.service.interfaces.AdminAnalyticsService
import java.math.BigDecimal

@Service
class AdminAnalyticsServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository
) : AdminAnalyticsService {

    @Transactional(readOnly = true)
    override fun getSalesSummary(): SalesSummaryDTO {
        val totalOrders = orderRepository.count()
        val totalUnits  = orderProductRepository.totalUnitsSold()
        val totalAmount = orderProductRepository.totalSalesAmount()

        return SalesSummaryDTO(
            totalOrders = totalOrders,
            totalUnitsSold = totalUnits,
            totalSalesAmount = totalAmount
        )
    }

    @Transactional(readOnly = true)
    override fun getProductSales(articleNumber: Long): ProductSalesDTO {
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw IllegalArgumentException("Product with article $articleNumber not found")

        return ProductSalesDTO(
            articleNumber   = product.articleNumber,
            productName     = product.name,
            totalUnitsSold  = orderProductRepository.unitsSoldByProduct(articleNumber),
            totalSalesAmount= orderProductRepository.salesAmountByProduct(articleNumber)
        )
    }

    @Transactional(readOnly = true)
    override fun getOrderStatusAnalytics(): OrderStatusAnalyticsDTO {
        val list = orderRepository.countByStatuses()
        val map  = OrderStatus.entries.associateWith { status ->
            list.find { it.status == status }?.cnt ?: 0L
        }
        return OrderStatusAnalyticsDTO(statusCounts = map)
    }
}
