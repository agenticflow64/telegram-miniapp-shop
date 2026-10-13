import {
	HeartOutlined,
	SearchOutlined,
	ShoppingCartOutlined,
	UserOutlined,
} from '@ant-design/icons'
import { Link } from 'react-router-dom'
import styles from './Navbar.module.scss'

type NavItem = {
	path: string
	icon: React.ReactNode
	label: string
}

const navItems: NavItem[] = [
	{ path: '/', icon: <SearchOutlined title='Поиск' />, label: 'Поиск' },
	{
		path: '/wishlist',
		icon: <HeartOutlined title='Список желаемого' />,
		label: 'Избранное',
	},
	{
		path: '/profile',
		icon: <UserOutlined title='Профиль' />,
		label: 'Профиль',
	},
	{
		path: '/cart',
		icon: <ShoppingCartOutlined title='Корзина' />,
		label: 'Корзина',
	},
]

export const Navbar = ({ activePath }: { activePath: string }) => {
	return (
		<nav className={styles.navbar}>
			{navItems.map(item => (
				<Link
					key={item.path}
					to={item.path}
					className={`${styles.navItem} ${
						activePath === item.path ? styles.active : ''
					}`}
					aria-label={item.label}
				>
					{item.icon}
				</Link>
			))}
		</nav>
	)
}
