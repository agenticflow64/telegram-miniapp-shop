import { useQuery } from '@tanstack/react-query'
import {
	Button,
	Card,
	Divider,
	Form,
	Input,
	message,
	Select,
	Space,
	Table,
	Tag,
	Typography,
} from 'antd'
import React, { useMemo } from 'react'
import { CategoryService } from '../api/category'
import {
	useAddAttributes,
	useCreateOrUpdateCategory,
} from '../hooks/admin/useAdminCategories'

const { Title } = Typography

type AttrRow = { id: number; name: string; created: boolean }

export default function CategoriesPage() {
	const [form] = Form.useForm<{ categoryName: string; attributes: string[] }>()
	const [attrForm] = Form.useForm<{
		categoryId: number
		attributes: string[]
	}>()

	const createMutation = useCreateOrUpdateCategory()
	const addMutation = useAddAttributes()

	/* ─── список категорий (селект) ─── */
	const { data: categoriesResp } = useQuery({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})

	const categories = useMemo(
		() =>
			categoriesResp
				? Object.entries(categoriesResp.categories).map(([name, id]) => ({
						label: name,
						value: id,
				  }))
				: [],
		[categoriesResp]
	)

	/* ─── обработчики ─── */
	const handleCreate = async (values: {
		categoryName: string
		attributes: string[]
	}) => {
		try {
			const res = await createMutation.mutateAsync({
				categoryName: values.categoryName.trim(),
				attributes: values.attributes ?? [],
			})
			showResult(res)
			form.resetFields()
		} catch (e) {
			message.error((e as Error).message)
		}
	}

	const handleAddAttrs = async (categoryId: number, attrs: string[]) => {
		if (!attrs.length) return
		try {
			const res = await addMutation.mutateAsync({
				categoryId,
				body: { attributes: attrs },
			})
			showResult(res)
			attrForm.resetFields()
		} catch (e) {
			message.error((e as Error).message)
		}
	}

	const showResult = (dto: {
		categoryId: number
		categoryCreated: boolean
		attributes: AttrRow[]
	}) => {
		message.success(
			<>
				Категория&nbsp;
				<Tag color={dto.categoryCreated ? 'green' : 'blue'}>
					{dto.categoryId}
				</Tag>
				&nbsp;
				{dto.categoryCreated ? 'создана' : 'найдена'}
			</>
		)
		setTableData(dto.attributes)
	}

	/* ─── таблица результатов ─── */
	const [tableData, setTableData] = React.useState<AttrRow[]>([])
	const columns = [
		{ title: 'ID', dataIndex: 'id', width: 80 },
		{ title: 'Имя', dataIndex: 'name' },
		{
			title: 'Статус',
			dataIndex: 'created',
			width: 140,
			render: (created: boolean) => (
				<Tag color={created ? 'green' : 'blue'}>
					{created ? 'создан' : 'существовал'}
				</Tag>
			),
		},
	]

	return (
		<div style={{ padding: 24 }}>
			<Title level={3}>Категории / атрибуты</Title>

			<Space direction='vertical' size='large' style={{ width: 500 }}>
				{/* ───── Создать категорию ───── */}
				<Card title='Создать категорию (с атрибутами)'>
					<Form layout='vertical' form={form} onFinish={handleCreate}>
						<Form.Item
							name='categoryName'
							label='Название категории'
							rules={[{ required: true, message: 'Введите имя' }]}
						>
							<Input placeholder='Например «Тетрадь»' />
						</Form.Item>

						<Form.Item
							name='attributes'
							label='Атрибуты'
							extra='Enter – новый тег'
						>
							<Select
								mode='tags'
								open={false}
								placeholder='Производитель, Цвет…'
							/>
						</Form.Item>

						<Button
							type='primary'
							htmlType='submit'
							loading={createMutation.isPending}
							block
						>
							Отправить
						</Button>
					</Form>
				</Card>

				{/* ───── Добавить атрибуты ───── */}
				<Card title='Добавить атрибуты в существующую категорию'>
					<Form
						layout='vertical'
						form={attrForm}
						onFinish={vals => handleAddAttrs(vals.categoryId, vals.attributes)}
					>
						<Form.Item
							name='categoryId'
							label='Категория'
							rules={[{ required: true, message: 'Выберите' }]}
						>
							<Select options={categories} placeholder='Категория' />
						</Form.Item>

						<Form.Item
							name='attributes'
							label='Новые атрибуты'
							rules={[{ required: true, message: 'Минимум один атрибут' }]}
						>
							<Select mode='tags' open={false} placeholder='Цвет, Формат…' />
						</Form.Item>

						<Button
							type='primary'
							htmlType='submit'
							loading={addMutation.isPending}
							block
						>
							Добавить
						</Button>
					</Form>
				</Card>
			</Space>

			{/* ───── Вывод результата ───── */}
			<Divider />
			<Table
				dataSource={tableData}
				columns={columns}
				rowKey='id'
				bordered
				pagination={false}
				locale={{ emptyText: 'Пока ничего' }}
				style={{ maxWidth: 600 }}
			/>
		</div>
	)
}
