// src/pages/CheckoutPage/CheckoutPage.tsx
import { Button, Form, Input, Modal, Radio, Typography } from 'antd'
import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { CheckoutSummary } from '../../components/CheckoutSummary/CheckoutSummary'
import { useCartCheckout } from '../../hooks/useCart'
import { useOrderActions } from '../../hooks/useOrders'
import type { CartItem } from '../../types/cart'
import type { CreateOrderRequest } from '../../types/order'
import styles from './CheckoutPage.module.scss'

const { Title, Text } = Typography

interface LocationState {
	cartSnapshot?: {
		items: CartItem[]
	}
}

export const CheckoutPage = () => {
	const [form] = Form.useForm()
	const navigate = useNavigate()
	const location = useLocation()
	const state = location.state as LocationState | undefined
	const { createOrder } = useOrderActions()
	const { getCartSnapshot } = useCartCheckout()
	const [isModalVisible, setIsModalVisible] = useState(false)
	const [orderId, setOrderId] = useState<number | null>(null)

	const cartItems = (
		state?.cartSnapshot?.items ||
		getCartSnapshot()?.items ||
		[]
	).filter((item: CartItem) => item.quantity > 0)

	const totalAmount = cartItems.reduce(
		(sum: number, item: CartItem) => sum + item.price * item.quantity,
		0
	)

	const handleSubmit = async (values: {
		payment_method: CreateOrderRequest['payment_method']
		order_address: string
	}) => {
		try {
			const orderData: CreateOrderRequest = {
				payment_method: values.payment_method,
				order_address: values.order_address,
			}

			const order = await createOrder.mutateAsync(orderData)
			setOrderId(order.id)
			setIsModalVisible(true)
		} catch (error) {
			console.error('Order creation failed:', error)
		}
	}

	const handleModalClose = () => {
		setIsModalVisible(false)
		window.location.href = '/'
	}

	if (cartItems.length === 0) {
		return (
			<div className={styles.emptyCart}>
				<Text type='secondary'>Ваша корзина пуста</Text>
				<Button type='primary' onClick={() => navigate('/')}>
					Вернуться к покупкам
				</Button>
			</div>
		)
	}

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Оформление заказа
			</Title>

			<CheckoutSummary items={cartItems} />

			<Form
				form={form}
				layout='vertical'
				onFinish={handleSubmit}
				className={styles.form}
			>
				<Form.Item
					name='payment_method'
					label='Способ оплаты'
					rules={[{ required: true, message: 'Выберите способ оплаты' }]}
				>
					<Radio.Group>
						<Radio.Button value='CASH'>Наличные</Radio.Button>
						<Radio.Button value='CARD'>Картой при получении</Radio.Button>
					</Radio.Group>
				</Form.Item>

				<Form.Item
					name='order_address'
					label='Адрес доставки'
					rules={[
						{ required: true, message: 'Укажите адрес доставки' },
						{ min: 10, message: 'Адрес слишком короткий' },
					]}
				>
					<Input.TextArea rows={3} placeholder='Улица, дом, квартира' />
				</Form.Item>

				<div className={styles.total}>
					<Text strong>К оплате:</Text>
					<Text strong>{totalAmount.toFixed(2)} ₽</Text>
				</div>

				<Button
					type='primary'
					size='large'
					htmlType='submit'
					loading={createOrder.isPending}
					block
				>
					Подтвердить заказ
				</Button>
			</Form>

			<Modal
				title='Заказ оформлен'
				open={isModalVisible}
				onOk={handleModalClose}
				onCancel={handleModalClose}
				footer={[
					<Button key='submit' type='primary' onClick={handleModalClose}>
						Хорошо
					</Button>,
				]}
			>
				<p>Ваш заказ №{orderId} успешно оформлен!</p>
				<p>Сумма заказа: {totalAmount.toFixed(2)} ₽</p>
				<p>Вы можете отслеживать статус заказа в разделе "Мои заказы".</p>
			</Modal>
		</div>
	)
}
