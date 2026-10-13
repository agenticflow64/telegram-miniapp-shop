import { DeleteOutlined, SearchOutlined } from '@ant-design/icons'
import {
	Button,
	Card,
	Empty,
	Input,
	List,
	message,
	Popconfirm,
	Spin,
	Typography,
} from 'antd'
import { useState } from 'react'
import {
	useDeleteProduct,
	useSearchProducts,
} from '../hooks/admin/useAdminDelete'
import { useDebounce } from '../hooks/useDebounce'
import { ProductResponse } from '../types/product'
const { Title, Text } = Typography

export default function DeleteProductPage() {
	const [q, setQ] = useState('')
	const debounced = useDebounce(q, 400)

	const { data, fetchNextPage, hasNextPage, isFetchingNextPage, isLoading } =
		useSearchProducts(debounced)

	const deleteMut = useDeleteProduct()

	const products: ProductResponse[] = data?.pages.flatMap(p => p.products) || []

	const handleDelete = async (article: number) => {
		try {
			await deleteMut.mutateAsync(article)
			message.success(`Товар ${article} удалён`)
		} catch (e) {
			console.error(e)
		}
	}

	return (
		<div style={{ padding: 24, maxWidth: 900 }}>
			<Title level={3}>Удалить товар</Title>

			<Input
				prefix={<SearchOutlined />}
				placeholder='Поиск по названию / артикулу'
				value={q}
				onChange={e => setQ(e.target.value)}
				allowClear
				style={{ marginBottom: 24 }}
			/>

			{isLoading ? (
				<Spin />
			) : products.length === 0 ? (
				<Empty description='Ничего не найдено' />
			) : (
				<List
					dataSource={products}
					renderItem={item => (
						<List.Item>
							<Card
								style={{ width: '100%' }}
								bodyStyle={{ display: 'flex', gap: 16 }}
							>
								<img
									src={item.photos[0] || '/placeholder-product.jpg'}
									alt=''
									style={{ width: 80, height: 80, objectFit: 'cover' }}
								/>

								<div style={{ flex: 1 }}>
									<Text strong>{item.name}</Text>
									<br />
									<Text type='secondary'>Артикул: {item.article_number}</Text>
									<br />
									<Text>Цена: {item.price} ₽</Text>
								</div>

								<Popconfirm
									title='Удалить товар?'
									okText='Да'
									cancelText='Нет'
									onConfirm={() => handleDelete(item.article_number)}
									okButtonProps={{ loading: deleteMut.isPending }}
								>
									<Button
										danger
										icon={<DeleteOutlined />}
										loading={
											deleteMut.isPending &&
											deleteMut.variables === item.article_number
										}
									>
										Удалить
									</Button>
								</Popconfirm>
							</Card>
						</List.Item>
					)}
					loadMore={
						hasNextPage && (
							<div style={{ textAlign: 'center', margin: 16 }}>
								<Button
									onClick={() => fetchNextPage()}
									loading={isFetchingNextPage}
								>
									Показать ещё
								</Button>
							</div>
						)
					}
				/>
			)}
		</div>
	)
}
