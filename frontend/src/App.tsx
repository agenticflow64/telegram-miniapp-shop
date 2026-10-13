import 'antd/dist/reset.css'
import { useEffect } from 'react'
import { useAuth } from './hooks/useAuth'
import { useInitialData } from './hooks/useInitialData'
import initializeTelegramApp from './utils/initTelegram'

//

import { Route, Routes } from 'react-router-dom'
import { NavbarContainer } from './containers/NavbarContainer/NavbarContainer'
import { CartPage } from './pages/CartPage/CartPage'
import { CheckoutPage } from './pages/CheckoutPage/CheckoutPage'
import { HomePage } from './pages/HomePage/HomePage'
import { LogoutPage } from './pages/LogoutPage/LogoutPage'
import { OrdersPage } from './pages/OrdersPage/OrdersPage'
import { ProductPage } from './pages/ProductPage/ProductPage'
import { ProfilePage } from './pages/ProfilePage/ProfilePage'
import { WishlistPage } from './pages/WishListPage/WishlistPage'

export default function App() {
	useEffect(() => {
		initializeTelegramApp()
	}, [])

	const { authData, isAuthLoading, authError } = useAuth()
	const queries = useInitialData()

	useEffect(() => {
		if (authData?.isAuthenticated) {
			const [products, orders, wishlist, cart] = queries
			console.log('Initial data loaded:', {
				products: products.data,
				orders: orders.data,
				wishlist: wishlist.data,
				cart: cart.data,
			})
		}
	}, [queries, authData])

	if (isAuthLoading) {
		return <div>Authenticating with Telegram...</div>
	}

	if (authError) {
		return (
			<div>
				Authentication failed: {authError.message}
				<button onClick={() => window.location.reload()}>Retry</button>
			</div>
		)
	}

	return (
		<>
			<Routes>
				<Route path='/' element={<HomePage />} />
				<Route path='/wishlist' element={<WishlistPage />} />
				<Route path='/profile' element={<ProfilePage />} />
				<Route path='/cart' element={<CartPage />} />
				<Route path='/product/:article' element={<ProductPage />} />
				<Route path='/checkout' element={<CheckoutPage />} />
				<Route path='/orders' element={<OrdersPage />} />
				<Route path='/logout' element={<LogoutPage />} />
			</Routes>
			<NavbarContainer />
		</>
	)
}
