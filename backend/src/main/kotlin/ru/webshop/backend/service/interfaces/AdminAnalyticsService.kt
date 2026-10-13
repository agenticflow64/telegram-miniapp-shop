package ru.webshop.backend.service.interfaces

import ru.webshop.backend.dto.admin.*

interface AdminAnalyticsService {
    fun getSalesSummary(): SalesSummaryDTO
    fun getProductSales(articleNumber: Long): ProductSalesDTO
    fun getOrderStatusAnalytics(): OrderStatusAnalyticsDTO
}
