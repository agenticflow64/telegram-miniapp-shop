// src/components/CheckoutSummary/CheckoutSummary.tsx
import { Card, Typography } from 'antd'
import { Link } from 'react-router-dom'
import { CartItemComponent } from '../CartItem/CartItemComponent'
import styles from './CheckoutSummary.module.scss'

const { Title, Text } = Typography

type CheckoutSummaryProps = {
	items: Array<{
		article_number: number
		quantity: number
		price: number
		product_name: string
	}>
	onRemove?: (article: number) => void
	onQuantityChange?: (article: number, delta: number) => void
	editable?: boolean
}

export const CheckoutSummary = ({
	items,
	onRemove,
	onQuantityChange,
	editable = false,
}: CheckoutSummaryProps) => {
	const totalAmount = items.reduce(
		(sum, item) => sum + item.price * item.quantity,
		0
	)

	return (
		<Card className={styles.summaryCard}>
			<Title level={4} className={styles.title}>
				Состав заказа
			</Title>

			<div className={styles.itemsList}>
				{items.map(item => (
					<div key={item.article_number} className={styles.itemWrapper}>
						<CartItemComponent
							article={item.article_number}
							quantity={item.quantity}
							onRemove={onRemove || (() => {})}
							onQuantityChange={onQuantityChange || (() => {})}
							editable={false}
						/>
					</div>
				))}
			</div>

			<div className={styles.totalSection}>
				<div className={styles.totalRow}>
					<Text>Товары ({items.length})</Text>
					<Text>{totalAmount.toFixed(2)} ₽</Text>
				</div>
				<div className={styles.totalRow}>
					<Text strong>Итого</Text>
					<Text strong>{totalAmount.toFixed(2)} ₽</Text>
				</div>
			</div>

			{editable && (
				<Link to='/cart' className={styles.editLink}>
					Редактировать заказ
				</Link>
			)}
		</Card>
	)
}
