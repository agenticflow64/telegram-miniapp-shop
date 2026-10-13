// src/pages/CreateProductPage.tsx
import { PlusOutlined } from '@ant-design/icons'
import { useQuery } from '@tanstack/react-query'
import {
	Button,
	Card,
	Form,
	Input,
	InputNumber,
	message,
	Select,
	Space,
	Typography,
	Upload,
	UploadFile,
} from 'antd'
import { RcFile } from 'antd/es/upload'
import { useMemo, useState } from 'react'
import { CategoryService } from '../api/category'
import {
	useCreateProduct,
	useUploadPhoto,
} from '../hooks/admin/useAdminProducts'
import { ProductCreateRequestDTO } from '../types/product'

const { Title } = Typography

export default function CreateProductPage() {
	/* ─────────── формы ─────────── */
	const [form] = Form.useForm()

	/* ─────────── категории (селект) ─────────── */
	const { data: categoriesResp } = useQuery({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})

	const categoriesOptions = useMemo(
		() =>
			categoriesResp
				? Object.entries(categoriesResp.categories).map(([name, id]) => ({
						label: name,
						value: id,
				  }))
				: [],
		[categoriesResp]
	)

	/* ─────────── динамические атрибуты ─────────── */
	/** текущее значение categoryId прямо из формы  */
	const categoryId: number | undefined = Form.useWatch('categoryId', form)

	const { data: attrsResp, isFetching: attrsLoading } = useQuery({
		queryKey: ['category-attrs', categoryId],
		queryFn: () => CategoryService.getAttributes(categoryId!), // ! безопасно: enabled гарантирует
		enabled: !!categoryId,
		staleTime: Infinity,
	})

	/* ─────────── файлы ─────────── */
	const [fileList, setFileList] = useState<UploadFile<RcFile>[]>([])

	const beforeUpload = (file: RcFile) => {
		if (fileList.length >= 5) {
			message.warning('Максимум 5 фото')
			return Upload.LIST_IGNORE
		}
		setFileList(prev => [...prev, file])
		return false // отменяем авто-загрузку
	}

	const removeFile = (file: UploadFile) =>
		setFileList(prev => prev.filter(f => f.uid !== file.uid))

	/* ─────────── мутации ─────────── */
	const createMut = useCreateProduct()
	const uploadMut = useUploadPhoto()

	/* ─────────── submit ─────────── */
	const onFinish = async (values: Record<string, unknown>) => {
		try {
			/* мапа attrId → value */
			const attributes: Record<string, string> = {}
			attrsResp?.attributes.forEach(a => {
				const v = values[`attr_${a.id}`] as string | undefined
				if (v) attributes[a.id.toString()] = v
			})

			const dto: ProductCreateRequestDTO = {
				name: values.name as string,
				price: Number(values.price),
				balance_in_stock: Number(values.balance),
				description: values.description as string | undefined,
				category_id: values.categoryId as number, // ← гарантированно валидный
				attributes,
				number_of_orders: 0,
				rating: 0,
			}

			/* создаём товар */
			const article = await createMut.mutateAsync(dto)

			/* грузим фото (параллельно) */
			await Promise.all(
				fileList.map((f, i) =>
					uploadMut.mutateAsync({
						meta: { articleNumber: article, indexNumber: i + 1 },
						file: f as RcFile,
					})
				)
			)

			message.success(`Товар ${article} создан!`)
			form.resetFields()
			setFileList([])
		} catch (e) {
			message.error((e as Error).message)
		}
	}

	/* ─────────── UI ─────────── */
	return (
		<div style={{ padding: 24, maxWidth: 800 }}>
			<Title level={3}>Новый товар</Title>

			<Card>
				<Form
					form={form}
					layout='vertical'
					onFinish={onFinish}
					disabled={createMut.isPending || uploadMut.isPending}
				>
					<Form.Item
						name='categoryId'
						label='Категория'
						rules={[{ required: true, message: 'Выберите категорию' }]}
					>
						<Select options={categoriesOptions} placeholder='—' />
					</Form.Item>

					<Form.Item
						name='name'
						label='Название'
						rules={[{ required: true, min: 3 }]}
					>
						<Input />
					</Form.Item>

					<Form.Item
						name='price'
						label='Цена, ₽'
						rules={[{ required: true, type: 'number', min: 0 }]}
					>
						<InputNumber style={{ width: '100%' }} />
					</Form.Item>

					<Form.Item
						name='balance'
						label='Количество на складе'
						rules={[{ required: true, type: 'number', min: 0 }]}
					>
						<InputNumber style={{ width: '100%' }} />
					</Form.Item>

					<Form.Item name='description' label='Описание'>
						<Input.TextArea rows={3} />
					</Form.Item>

					{/* динамические поля атрибутов */}
					{attrsLoading && <p>Загрузка атрибутов…</p>}
					{attrsResp?.attributes.map(attr => (
						<Form.Item
							key={attr.id}
							name={`attr_${attr.id}`}
							label={attr.name}
							rules={[{ required: true }]}
						>
							{attr.values.length ? (
								<Select
									options={attr.values.map(v => ({ label: v, value: v }))}
									placeholder='—'
								/>
							) : (
								<Input />
							)}
						</Form.Item>
					))}

					{/* фото */}
					<Form.Item
						label='Фотографии (до 5 шт)'
						required
						validateStatus={!fileList.length ? 'error' : ''}
						help={!fileList.length && 'Добавьте минимум одну фотографию'}
					>
						<Upload
							listType='picture-card'
							fileList={fileList}
							beforeUpload={beforeUpload}
							onRemove={removeFile}
							accept='image/*'
						>
							{fileList.length >= 5 ? null : (
								<div>
									<PlusOutlined />
									<div style={{ marginTop: 8 }}>Добавить</div>
								</div>
							)}
						</Upload>
					</Form.Item>

					<Space direction='vertical' style={{ width: '100%' }}>
						<Button
							type='primary'
							htmlType='submit'
							block
							loading={createMut.isPending || uploadMut.isPending}
							disabled={!fileList.length}
						>
							Сохранить
						</Button>
					</Space>
				</Form>
			</Card>
		</div>
	)
}
