import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './auth/AuthProvider'
import { PrivateRoute } from './auth/PrivateRoute'

import AdminLayout from './components/AdminLayout/AdminLayout'
import AnalyticsPage from './pages/AnalyticsPage'
import CategoriesPage from './pages/CategoriesPage'
import CreateProductPage from './pages/CreateProductPage'
import DashboardPage from './pages/DashboardPage'
import DeleteProductPage from './pages/DeleteProductPage'
import LoginPage from './pages/LoginPage'

const queryClient = new QueryClient()

export default function App() {
	return (
		<QueryClientProvider client={queryClient}>
			<BrowserRouter>
				<AuthProvider>
					<Routes>
						{/* публичный */}
						<Route path='/login' element={<LoginPage />} />

						{/* всё, что ниже – только для авторизованных */}
						<Route element={<PrivateRoute />}>
							<Route element={<AdminLayout />}>
								<Route index element={<DashboardPage />} />
								<Route path='categories' element={<CategoriesPage />} />
								<Route path='products'>
									<Route path='new' element={<CreateProductPage />} />
									<Route path='delete' element={<DeleteProductPage />} />
								</Route>
								<Route path='analytics' element={<AnalyticsPage />} />
							</Route>
						</Route>
					</Routes>
				</AuthProvider>
			</BrowserRouter>
		</QueryClientProvider>
	)
}
