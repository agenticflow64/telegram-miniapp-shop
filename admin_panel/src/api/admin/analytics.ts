import {
	OrderStatusAnalyticsDTO,
	ProductSalesDTO,
	SalesSummaryDTO,
} from '../../types/admin/analytics'
import api from '../axios'

export const AdminAnalyticsApi = {
	summary: () =>
		api.get<SalesSummaryDTO>('/analytics/summary').then(r => r.data),

	product: (article: number) =>
		api.get<ProductSalesDTO>(`/analytics/product/${article}`).then(r => r.data),

	ordersByStatus: () =>
		api
			.get<OrderStatusAnalyticsDTO>('/analytics/orders/by-status')
			.then(r => r.data),
}
