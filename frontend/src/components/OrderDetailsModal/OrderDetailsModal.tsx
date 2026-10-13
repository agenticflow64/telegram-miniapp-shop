import { Button, Image, List, message, Modal, Tag, Typography } from 'antd'
import { useOrderActions } from '../../hooks/useOrders'
import { useProductDetails } from '../../hooks/useProducts'
import { OrderDTO, OrderItemDTO, OrderStatus } from '../../types/order'
import styles from './OrderDetailsModal.module.scss'

const { Text, Title } = Typography

const cancellableStatuses: OrderStatus[] = [
	'CREATED',
	'WAITING_FOR_PAYMENT',
	'PAID',
	'PROCESSING',
	'IN_PROGRESS',
]

const statusColors: Record<OrderStatus, string> = {
	CREATED: 'green',
	WAITING_FOR_PAYMENT: 'orange',
	PAID: 'blue',
	PROCESSING: 'purple',
	IN_PROGRESS: 'purple',
	IN_WAY: 'geekblue',
	DELIVERED: 'green',
	CANCELLED: 'red',
}

const statusLabels: Record<OrderStatus, string> = {
	CREATED: 'Создан',
	WAITING_FOR_PAYMENT: 'Ожидает оплаты',
	PAID: 'Оплачен',
	PROCESSING: 'В обработке',
	IN_PROGRESS: 'В работе',
	IN_WAY: 'В пути',
	DELIVERED: 'Доставлен',
	CANCELLED: 'Отменён',
}

type OrderDetailsModalProps = {
	order: OrderDTO | null
	visible: boolean
	onClose: () => void
	onOrderCancel?: (orderId: number) => void
}

export const OrderDetailsModal = ({
	order,
	visible,
	onClose,
	onOrderCancel,
}: OrderDetailsModalProps) => {
	const { cancelOrder } = useOrderActions()

	const handleCancelOrder = async () => {
		if (!order) return

		try {
			await cancelOrder.mutateAsync(order.id)
			message.success('Заказ успешно отменен')
			onOrderCancel?.(order.id)
			onClose()
			// eslint-disable-next-line @typescript-eslint/no-unused-vars
		} catch (error) {
			message.error('Не удалось отменить заказ')
		}
	}

	if (!order) return null

	const showCancelButton = cancellableStatuses.includes(order.status)

	return (
		<Modal
			title={`Заказ №${order.id}`}
			open={visible}
			onCancel={onClose}
			footer={[
				<Button key='close' onClick={onClose}>
					Закрыть
				</Button>,
			]}
			width={800}
		>
			<div className={styles.statusContainer}>
				<Tag color={statusColors[order.status]} className={styles.statusTag}>
					{statusLabels[order.status]}
				</Tag>
				<Text type='secondary'>
					{new Date(order.created_at).toLocaleString()}
				</Text>
			</div>

			<div className={styles.section}>
				<Title level={5} className={styles.sectionTitle}>
					Детали доставки
				</Title>
				<div className={styles.detailsContainer}>
					<div className={styles.detailRow}>
						<Text strong>Адрес: </Text>
						<Text>{order.order_address}</Text>
					</div>
					<div className={styles.detailRow}>
						<Text strong>Способ оплаты: </Text>
						<Text>
							{order.payment_method === 'CASH'
								? 'Наличные'
								: order.payment_method === 'CARD'
								? 'Картой при получении'
								: 'Криптовалюта'}
						</Text>
					</div>
				</div>
			</div>

			<div className={styles.section}>
				<Title level={5} className={styles.sectionTitle}>
					Товары ({order.items.length})
				</Title>
				<List
					dataSource={order.items}
					renderItem={item => (
						<OrderItemDetails key={item.article_number} item={item} />
					)}
				/>
			</div>

			<div className={styles.total}>
				<Text strong>Итого:</Text>
				<Title level={4} style={{ margin: 0 }}>
					{order.order_amount.toFixed(2)} ₽
				</Title>
			</div>
			{showCancelButton && (
				<Button
					danger
					block
					onClick={handleCancelOrder}
					loading={cancelOrder.isPending}
					className={styles.cancelButton}
				>
					Отменить заказ
				</Button>
			)}
		</Modal>
	)
}

const OrderItemDetails = ({ item }: { item: OrderItemDTO }) => {
	const { data: product } = useProductDetails(item.article_number)

	return (
		<List.Item className={styles.listItem}>
			<div className={styles.itemContent}>
				<div className={styles.itemImage}>
					{product?.photos?.[0] ? (
						<Image
							src={product.photos[0]}
							alt={item.product_name}
							width={64}
							height={64}
							preview={false}
						/>
					) : (
						<div className={styles.imagePlaceholder} />
					)}
				</div>
				<div className={styles.itemInfo}>
					<Text strong>{item.product_name}</Text>
					<div className={styles.itemMeta}>
						<Text>
							{item.quantity} × {item.price.toFixed(2)} ₽
						</Text>
						<Text strong>{(item.quantity * item.price).toFixed(2)} ₽</Text>
					</div>
				</div>
			</div>
		</List.Item>
	)
}
