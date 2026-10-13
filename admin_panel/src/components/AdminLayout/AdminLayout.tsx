import {
	AppstoreOutlined,
	BarChartOutlined,
	DeleteOutlined,
	FolderOpenOutlined,
	LogoutOutlined,
	PlusSquareOutlined,
} from '@ant-design/icons'
import { Button, Layout, Menu } from 'antd'
import { Link, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'
import styles from './AdminLayout.module.scss'

const { Sider, Header, Content } = Layout

export default function AdminLayout() {
	const { pathname } = useLocation()
	const { logout } = useAuth()

	/** определяем активный пункт меню */
	const key = (() => {
		if (pathname === '/') return 'dashboard'
		const [, first, second] = pathname.split('/') // '', 'products', 'new'
		return first === 'products' ? `${first}-${second}` : first
	})()

	return (
		<Layout style={{ minHeight: '100vh' }}>
			<Sider breakpoint='lg' collapsedWidth={80}>
				<div className={styles.logo}>Admin</div>

				<Menu
					theme='dark'
					mode='inline'
					selectedKeys={[key]}
					items={[
						{
							key: 'dashboard',
							icon: <AppstoreOutlined />,
							label: <Link to='/'>Главная</Link>,
						},
						{
							key: 'categories',
							icon: <FolderOpenOutlined />,
							label: <Link to='/categories'>Категории</Link>,
						},
						{
							key: 'products-new',
							icon: <PlusSquareOutlined />,
							label: <Link to='/products/new'>Новый&nbsp;товар</Link>,
						},
						{
							key: 'products-delete',
							icon: <DeleteOutlined />,
							label: <Link to='/products/delete'>Удалить&nbsp;товар</Link>,
						},
						{
							key: 'analytics',
							icon: <BarChartOutlined />,
							label: <Link to='/analytics'>Аналитика</Link>,
						},
					]}
				/>
			</Sider>

			<Layout>
				<Header className={styles.header}>
					<span className={styles.title}>Админ-панель</span>
					<Button icon={<LogoutOutlined />} onClick={logout}>
						Выход
					</Button>
				</Header>

				<Content className={styles.content}>
					{/* сюда будут рендериться все «внутренние» страницы */}
					<Outlet />
				</Content>
			</Layout>
		</Layout>
	)
}
