import { useQuery } from '@tanstack/react-query'
import { AdminAnalyticsApi } from '../../api/admin/analytics'

export const useSalesSummary = () =>
	useQuery({
		queryKey: ['analytics', 'summary'],
		queryFn: AdminAnalyticsApi.summary,
	})

export const useProductAnalytics = (article?: number) =>
	useQuery({
		queryKey: ['analytics', 'product', article],
		queryFn: () => AdminAnalyticsApi.product(article!),
		enabled: !!article,
	})

export const useOrderStatus = () =>
	useQuery({
		queryKey: ['analytics', 'order-status'],
		queryFn: AdminAnalyticsApi.ordersByStatus,
	})
