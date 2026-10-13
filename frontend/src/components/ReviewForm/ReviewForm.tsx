import { UploadOutlined } from '@ant-design/icons'
import { Button, Form, Input, Modal, Rate, Upload, message } from 'antd'
import { useState } from 'react'
import styles from './ReviewForm.module.scss'

type ReviewFormValues = {
	rating: number
	text: string
	photos: File[]
}

export const ReviewForm = ({
	visible,
	onCancel,
	onSubmit,
	loading,
}: {
	visible: boolean
	onCancel: () => void
	onSubmit: (values: ReviewFormValues) => void
	loading: boolean
}) => {
	const [form] = Form.useForm()
	const [files, setFiles] = useState<File[]>([])

	const beforeUpload = (file: File) => {
		const isImage = file.type.startsWith('image/')
		if (!isImage) message.error('Можно загружать только изображения!')
		return isImage
	}

	return (
		<Modal
			title='Оставить отзыв'
			open={visible}
			onCancel={onCancel}
			footer={null}
			width={600}
		>
			<Form
				form={form}
				layout='vertical'
				onFinish={values => onSubmit({ ...values, photos: files })}
			>
				<Form.Item
					name='rating'
					label='Оценка'
					rules={[{ required: true, message: 'Поставьте оценку' }]}
				>
					<Rate allowHalf />
				</Form.Item>

				<Form.Item
					name='text'
					label='Текст отзыва'
					rules={[
						{ required: true, message: 'Напишите отзыв' },
						{ min: 10, message: 'Минимум 10 символов' },
						{ max: 1000, message: 'Максимум 1000 символов' },
					]}
				>
					<Input.TextArea rows={4} />
				</Form.Item>

				<Form.Item label='Фотографии (макс. 3)'>
					<Upload
						beforeUpload={file => {
							if (beforeUpload(file)) {
								setFiles([...files, file].slice(0, 3))
							}
							return false
						}}
						fileList={files.map((file, i) => ({
							uid: i.toString(),
							name: file.name,
							status: 'done',
						}))}
						onRemove={() => setFiles([])}
						multiple
						accept='image/*'
					>
						<Button icon={<UploadOutlined />}>Загрузить</Button>
					</Upload>
				</Form.Item>

				<div className={styles.footer}>
					<Button onClick={onCancel}>Отмена</Button>
					<Button type='primary' htmlType='submit' loading={loading}>
						Отправить отзыв
					</Button>
				</div>
			</Form>
		</Modal>
	)
}
