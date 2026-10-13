// src/pages/CartPage/CartPage.tsx
import { Button, Empty, Spin, Typography, message } from 'antd'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { CartItemComponent } from '../../components/CartItem/CartItemComponent'
import {
	useAllCartItems,
	useCart,
	useCartActions,
	useCartCheckout,
} from '../../hooks/useCart'
import styles from './CartPage.module.scss'

const { Title, Text } = Typography

export const CartPage = () => {
	const [page, setPage] = useState(0)
	const { data, isLoading, isFetching } = useCart(page)
	const { data: allItems } = useAllCartItems()
	const { removeFromCart, addToCart } = useCartActions()
	const { getCartSnapshot } = useCartCheckout()

	const handleLoadMore = () => setPage(prev => prev + 1)
	const handleRemove = (article: number) => {
		removeFromCart.mutate(article, {
			onSuccess: () => {
				message.success('Товар удален из корзины')
			},
		})
	}

	const handleQuantityChange = (article: number, delta: number) => {
		addToCart.mutate(
			{ article_number: article, quantity: delta },
			{
				onSuccess: () => {
					message.success('Количество товара изменено')
				},
			}
		)
	}

	const cartItems = allItems?.items || data?.items || []
	const totalAmount = cartItems.reduce(
		(sum, item) => sum + item.price * item.quantity,
		0
	)

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Корзина
			</Title>

			{totalAmount > 0 && (
				<div className={styles.summary}>
					<div className={styles.summaryContent}>
						<Text strong className={styles.totalText}>
							Итого: {totalAmount.toFixed(2)} ₽
						</Text>
						<Text type='secondary' className={styles.itemsCount}>
							{cartItems.length} товаров
						</Text>
					</div>
					<Link to='/checkout' state={{ cartSnapshot: getCartSnapshot() }}>
						<Button type='primary' size='large' block>
							Оформить заказ
						</Button>
					</Link>
				</div>
			)}

			{isLoading ? (
				<Spin size='large' className={styles.spinner} />
			) : (
				<>
					<div className={styles.list}>
						{cartItems.map(item => (
							<CartItemComponent
								key={item.article_number}
								article={item.article_number}
								quantity={item.quantity}
								onRemove={handleRemove}
								onQuantityChange={handleQuantityChange}
							/>
						))}
					</div>

					{data && data.current_page < data.total_pages && (
						<div className={styles.loadMore}>
							<Button
								type='primary'
								loading={isFetching}
								onClick={handleLoadMore}
							>
								Показать еще
							</Button>
						</div>
					)}
				</>
			)}

			{!isLoading && cartItems.length === 0 && (
				<Empty
					image={Empty.PRESENTED_IMAGE_SIMPLE}
					description={
						<div className={styles.emptyContent}>
							<Text type='secondary'>Корзина пуста</Text>
							<Link to='/'>
								<Button type='primary' style={{ marginTop: 16 }}>
									Перейти к покупкам
								</Button>
							</Link>
						</div>
					}
				/>
			)}
		</div>
	)
}
