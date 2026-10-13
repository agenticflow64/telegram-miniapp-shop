// enum/union уже был в types/order.ts
export type OrderStatus =
	| 'CREATED'
	| 'WAITING_FOR_PAYMENT'
	| 'PAID'
	| 'PROCESSING'
	| 'IN_PROGRESS'
	| 'IN_WAY'
	| 'DELIVERED'
	| 'CANCELLED'
/* ───────────────────── SalesSummaryDTO ───────────────────── */

export interface SalesSummaryDTO {
	/** оформленных (НЕ отменённых) заказов */
	total_orders: number
	/** сколько единиц товара продано в сумме */
	total_units_sold: number
	/** итоговая выручка (decimal на backend, число у нас) */
	total_sales_amount: number
}

/* ───────────────────── ProductSalesDTO ──────────────────── */

export interface ProductSalesDTO {
	/** артикул */
	article_number: number
	/** название */
	product_name: string
	/** сколько штук продано */
	total_units_sold: number
	/** выручка по данному товару */
	total_sales_amount: number
}

/* ────────────────── OrderStatusAnalyticsDTO ─────────────── */

export interface OrderStatusAnalyticsDTO {
	/**
	 * карта статус ⇒ количество.
	 * пример: { IN_PROGRESS: 45, PAID: 32, CANCELLED: 8 }
	 */
	status_counts: Record<OrderStatus, number>
}
