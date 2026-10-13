// src/pages/OrdersPage/OrdersPage.tsx
import { Spin, Typography } from 'antd'
import { useState } from 'react'
import { OrderCard } from '../../components/OrderCard/OrderCard'
import { OrderDetailsModal } from '../../components/OrderDetailsModal/OrderDetailsModal'
import { useOrders } from '../../hooks/useOrders'
import { OrderDTO } from '../../types/order'
import styles from './OrdersPage.module.scss'

const { Title } = Typography

export const OrdersPage = () => {
	const [selectedOrder, setSelectedOrder] = useState<OrderDTO | null>(null)
	const { data, isLoading, refetch } = useOrders(0)

	if (isLoading) {
		return <Spin size='large' className={styles.spinner} />
	}

	const handleOrderCancel = (orderId: number) => {
		console.log(orderId)
		refetch()
	}

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Мои заказы
			</Title>

			<div className={styles.ordersList}>
				{data?.orders.map(order => (
					<OrderCard
						key={order.id}
						order={order}
						onClick={() => setSelectedOrder(order)}
					/>
				))}
			</div>

			{data?.orders.length === 0 && (
				<div className={styles.empty}>
					<div>У вас пока нет заказов</div>
				</div>
			)}

			<OrderDetailsModal
				order={selectedOrder}
				visible={!!selectedOrder}
				onClose={() => setSelectedOrder(null)}
				onOrderCancel={handleOrderCancel}
			/>
		</div>
	)
}
