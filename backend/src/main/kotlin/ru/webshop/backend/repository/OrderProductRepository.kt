package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.webshop.backend.entity.OrderProduct
import java.math.BigDecimal

interface OrderProductRepository: JpaRepository<OrderProduct, Long> {
    //––– ОБЩИЕ ПРОДАЖИ ––––––––––––––––––––––––––––––––––––––––––
    @Query("select coalesce(sum(op.quantity),0) from OrderProduct op " +
            "where op.order.status <> ru.webshop.backend.enums.OrderStatus.CANCELLED")
    fun totalUnitsSold(): Long

    @Query("select coalesce(sum(op.amount),0) from OrderProduct op " +
            "where op.order.status <> ru.webshop.backend.enums.OrderStatus.CANCELLED")
    fun totalSalesAmount(): BigDecimal

    //––– ПРОДАЖИ КОНКРЕТНОГО ТОВАРА ––––––––––––––––––––––––––––––
    @Query("select coalesce(sum(op.quantity),0) from OrderProduct op " +
            "where op.product.articleNumber = :article and " +
            "op.order.status <> ru.webshop.backend.enums.OrderStatus.CANCELLED")
    fun unitsSoldByProduct(@Param("article") article: Long): Long

    @Query("select coalesce(sum(op.amount),0) from OrderProduct op " +
            "where op.product.articleNumber = :article and " +
            "op.order.status <> ru.webshop.backend.enums.OrderStatus.CANCELLED")
    fun salesAmountByProduct(@Param("article") article: Long): BigDecimal
}