package ru.webshop.backend.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.dto.admin.OrderStatusAnalyticsDTO
import ru.webshop.backend.dto.admin.ProductSalesDTO
import ru.webshop.backend.dto.admin.SalesSummaryDTO
import ru.webshop.backend.service.interfaces.AdminAnalyticsService

@RestController
@RequestMapping("/api/admin/analytics")
@Tag(name = "AdminAnalytics", description = "Аналитика для администратора")
class AdminAnalyticsController(
    private val analyticsService: AdminAnalyticsService
) {
    @Operation(summary = "Общая аналитика продаж")
    @GetMapping("/summary")
    fun summary(): SalesSummaryDTO = analyticsService.getSalesSummary()

    @Operation(summary = "Аналитика по конкретному товару")
    @GetMapping("/product/{articleNumber}")
    fun productAnalytics(@PathVariable articleNumber: Long): ProductSalesDTO =
        analyticsService.getProductSales(articleNumber)

    @Operation(summary = "Текущее распределение заказов по статусам")
    @GetMapping("/orders/by-status")
    fun ordersByStatus(): OrderStatusAnalyticsDTO = analyticsService.getOrderStatusAnalytics()
}