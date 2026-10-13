import { Badge, Card, Image, Space, Typography } from 'antd'
import { useProductDetails } from '../../hooks/useProducts'
import { OrderDTO } from '../../types/order'
import styles from './OrderCard.module.scss'

const { Text, Title } = Typography

const statusColors: Record<OrderDTO['status'], string> = {
	CREATED: 'green',
	WAITING_FOR_PAYMENT: 'orange',
	PAID: 'blue',
	PROCESSING: 'purple',
	IN_PROGRESS: 'purple',
	IN_WAY: 'geekblue',
	DELIVERED: 'green',
	CANCELLED: 'red',
}

const statusLabels: Record<OrderDTO['status'], string> = {
	CREATED: 'Создан',
	WAITING_FOR_PAYMENT: 'Ожидает оплаты',
	PAID: 'Оплачен',
	PROCESSING: 'В обработке',
	IN_PROGRESS: 'В работе',
	IN_WAY: 'В пути',
	DELIVERED: 'Доставлен',
	CANCELLED: 'Отменён',
}

type OrderCardProps = {
	order: OrderDTO
	onClick: () => void
}

export const OrderCard = ({ order, onClick }: OrderCardProps) => {
	return (
		<Card onClick={onClick} className={styles.card} hoverable>
			<Space direction='vertical' size='middle' style={{ width: '100%' }}>
				<div className={styles.header}>
					<Title level={5} className={styles.orderNumber}>
						Заказ №{order.id}
					</Title>
					<Badge
						color={statusColors[order.status]}
						text={statusLabels[order.status]}
					/>
				</div>

				<div className={styles.images}>
					{order.items.slice(0, 4).map((item, index) => (
						<OrderItemImage
							key={`${item.article_number}-${index}`}
							article={item.article_number}
							isLast={index === 3 && order.items.length > 4}
							remaining={order.items.length - 4}
						/>
					))}
				</div>

				<div className={styles.footer}>
					<Text>{new Date(order.created_at).toLocaleDateString()}</Text>
					<Text strong>{order.order_amount.toFixed(2)} ₽</Text>
				</div>
			</Space>
		</Card>
	)
}

const OrderItemImage = ({
	article,
	isLast,
	remaining,
}: {
	article: number
	isLast: boolean
	remaining: number
}) => {
	const { data: product } = useProductDetails(article)

	return (
		<div className={styles.imageContainer}>
			{product?.photos?.[0] ? (
				<Image
					src={product.photos[0]}
					alt=''
					width={64}
					height={64}
					preview={false}
					className={styles.image}
				/>
			) : (
				<div className={styles.imagePlaceholder} />
			)}
			{isLast && remaining > 0 && (
				<div className={styles.remainingBadge}>+{remaining}</div>
			)}
		</div>
	)
}
