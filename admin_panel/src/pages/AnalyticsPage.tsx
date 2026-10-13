import {
	Card,
	Col,
	Empty,
	InputNumber,
	message,
	Row,
	Segmented,
	Space,
	Spin,
	Statistic,
} from 'antd'
import { useEffect, useMemo, useState } from 'react'
import {
	Bar,
	BarChart,
	Cell,
	Legend,
	Pie,
	PieChart,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis,
} from 'recharts'
import {
	useOrderStatus,
	useProductAnalytics,
	useSalesSummary,
} from '../hooks/admin/useAdminAnalytics'

type Mode = 'summary' | 'product' | 'status'
type ProductMetric = 'units' | 'revenue'

const KPI_COLORS = ['#1890ff', '#13c2c2', '#ffc53d']
const STATUS_COLORS = [
	'#52c41a',
	'#faad14',
	'#f5222d',
	'#722ed1',
	'#1890ff',
	'#8c8c8c',
]

export default function AnalyticsPage() {
	const [mode, setMode] = useState<Mode>('summary')
	const [article, setArticle] = useState<number>()
	const [productMetric, setProductMetric] = useState<ProductMetric>('units')

	/* hooks */
	const summaryQ = useSalesSummary()
	const statusQ = useOrderStatus()
	const productQ = useProductAnalytics(article)

	const { error: summaryError } = summaryQ
	const { error: statusError } = statusQ
	const { error: productError } = productQ

	useEffect(() => {
		;[summaryError, statusError, productError]
			.filter(Boolean)
			.forEach(err => message.error((err as Error).message))
	}, [summaryError, statusError, productError])

	/* ------- Сводка: данные для графиков ------- */
	const ordersUnitsChartData = useMemo(
		() =>
			summaryQ.data
				? [
						{ metric: 'Заказы', value: summaryQ.data.total_orders },
						{ metric: 'Единицы', value: summaryQ.data.total_units_sold },
				  ]
				: [],
		[summaryQ.data]
	)

	const revenueChartData = useMemo(
		() =>
			summaryQ.data
				? [
						{
							metric: 'Выручка, ₽',
							value: Number(summaryQ.data.total_sales_amount),
						},
				  ]
				: [],
		[summaryQ.data]
	)

	/* ------- По товару: данные для графика ------- */
	const productChartData = useMemo(() => {
		if (!productQ.data) return []
		return productMetric === 'units'
			? [{ type: 'Шт', value: productQ.data.total_units_sold }]
			: [
					{
						type: '₽',
						value: Number(productQ.data.total_sales_amount),
					},
			  ]
	}, [productQ.data, productMetric])

	/* ------- Статусы ------- */
	const status_counts = statusQ.data?.status_counts ?? {}
	const statusChartData = useMemo(
		() =>
			Object.entries(status_counts).map(([status, cnt]) => ({
				status,
				cnt,
			})),
		[status_counts]
	)

	return (
		<Space
			direction='vertical'
			size='large'
			style={{ width: '100%', padding: 24 }}
		>
			<Segmented
				value={mode}
				onChange={val => setMode(val as Mode)}
				options={[
					{ label: 'Сводка', value: 'summary' },
					{ label: 'По товару', value: 'product' },
					{ label: 'Статусы заказов', value: 'status' },
				]}
			/>

			{/* ------- СВОДКА ------- */}
			{mode === 'summary' &&
				(summaryQ.isLoading ? (
					<Spin />
				) : summaryQ.data ? (
					<>
						<Row gutter={16}>
							<Col span={8}>
								<Statistic title='Заказов' value={summaryQ.data.total_orders} />
							</Col>
							<Col span={8}>
								<Statistic
									title='Единиц продано'
									value={summaryQ.data.total_units_sold}
								/>
							</Col>
							<Col span={8}>
								<Statistic
									title='Выручка, ₽'
									value={summaryQ.data.total_sales_amount}
									precision={2}
								/>
							</Col>
						</Row>

						<Row gutter={16}>
							{/* Заказы + Единицы */}
							<Col xs={24} md={12}>
								<Card title='Заказы и единицы'>
									<ResponsiveContainer width='100%' height={260}>
										<BarChart data={ordersUnitsChartData}>
											<XAxis dataKey='metric' tick={{ fontSize: 12 }} />
											<YAxis allowDecimals={false} />
											<Tooltip />
											<Bar dataKey='value'>
												{ordersUnitsChartData.map((_, idx) => (
													<Cell
														key={`cell-ou-${idx}`}
														fill={KPI_COLORS[idx % KPI_COLORS.length]}
													/>
												))}
											</Bar>
										</BarChart>
									</ResponsiveContainer>
								</Card>
							</Col>

							{/* Выручка */}
							<Col xs={24} md={12}>
								<Card title='Выручка, ₽'>
									<ResponsiveContainer width='100%' height={260}>
										<BarChart data={revenueChartData}>
											<XAxis dataKey='metric' tick={{ fontSize: 12 }} />
											<YAxis allowDecimals={false} />
											<Tooltip />
											<Bar dataKey='value' fill={KPI_COLORS[2]}>
												<Cell key='cell-revenue' fill={KPI_COLORS[2]} />
											</Bar>
										</BarChart>
									</ResponsiveContainer>
								</Card>
							</Col>
						</Row>
					</>
				) : (
					<Empty />
				))}

			{/* ------- ПО ТОВАРУ ------- */}
			{mode === 'product' && (
				<>
					<Space size='middle'>
						<InputNumber
							placeholder='Артикул товара'
							value={article}
							onChange={val => setArticle(val ?? undefined)}
							style={{ width: 200 }}
						/>

						{/* переключатель ₽ / Шт */}
						<Segmented
							value={productMetric}
							onChange={val => setProductMetric(val as ProductMetric)}
							options={[
								{ label: 'Шт', value: 'units' },
								{ label: '₽', value: 'revenue' },
							]}
						/>
					</Space>

					{productQ.isFetching ? (
						<Spin />
					) : productQ.data ? (
						<Card title={`Товар ${productQ.data.product_name}`}>
							<ResponsiveContainer width='100%' height={300}>
								<BarChart data={productChartData}>
									<XAxis dataKey='type' tick={{ fontSize: 12 }} />
									<YAxis allowDecimals={false} />
									<Tooltip />
									<Bar dataKey='value' fill='#1890ff' />
								</BarChart>
							</ResponsiveContainer>
						</Card>
					) : (
						<Empty description='Введите артикул' />
					)}
				</>
			)}

			{/* ------- СТАТУСЫ ------- */}
			{mode === 'status' &&
				(statusQ.isLoading ? (
					<Spin />
				) : statusChartData.length ? (
					<Card title='Заказы по статусам'>
						<ResponsiveContainer width='100%' height={350}>
							<PieChart>
								<Pie
									data={statusChartData}
									dataKey='cnt'
									nameKey='status'
									cx='50%'
									cy='50%'
									outerRadius='90%'
									label={({ percent }) => `${(percent * 100).toFixed(0)}%`}
								>
									{statusChartData.map((_, idx) => (
										<Cell
											key={`cell-status-${idx}`}
											fill={STATUS_COLORS[idx % STATUS_COLORS.length]}
										/>
									))}
								</Pie>
								<Legend />
								<Tooltip />
							</PieChart>
						</ResponsiveContainer>
					</Card>
				) : (
					<Empty description='Нет данных' />
				))}
		</Space>
	)
}
