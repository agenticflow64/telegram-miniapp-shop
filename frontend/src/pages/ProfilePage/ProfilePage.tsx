import {
	HeartOutlined,
	ShoppingCartOutlined,
	ShoppingOutlined,
} from '@ant-design/icons'
import { Button, List, Typography } from 'antd'
import { useNavigate } from 'react-router-dom'
import { UserProfileCard } from '../../components/UserProfile/UserProfileCard'
import { useTelegramUser } from '../../hooks/useTelegramAuth'
import styles from './ProfilePage.module.scss'

const { Title } = Typography

const menuItems = [
	{ label: 'Мои заказы', path: '/orders', icon: <ShoppingOutlined /> },
	{ label: 'Корзина', path: '/cart', icon: <ShoppingCartOutlined /> },
	{ label: 'Список желаний', path: '/wishlist', icon: <HeartOutlined /> },
]

export const ProfilePage = () => {
	const navigate = useNavigate()
	const user = useTelegramUser()

	if (!user) {
		return <div className={styles.loading}>Загрузка данных пользователя...</div>
	}

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Профиль
			</Title>

			<UserProfileCard user={user} />

			<List
				dataSource={menuItems}
				renderItem={item => (
					<List.Item className={styles.listItem}>
						<Button
							type='text'
							block
							onClick={() => navigate(item.path)}
							className={styles.menuButton}
							icon={item.icon}
						>
							{item.label}
						</Button>
					</List.Item>
				)}
			/>
		</div>
	)
}
