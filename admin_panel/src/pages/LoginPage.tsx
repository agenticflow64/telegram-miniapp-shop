import { Button, Card, Form, Input, Typography } from 'antd'
import { useState } from 'react'
import { useAuth } from '../auth/AuthContext'

const { Title } = Typography

export default function LoginPage() {
	const { login } = useAuth()
	const [loading, setLoading] = useState(false)

	const onFinish = async (values: { login: string; password: string }) => {
		setLoading(true)
		try {
			await login(values.login, values.password)
		} finally {
			setLoading(false)
		}
	}

	return (
		<div
			style={{
				minHeight: '100vh',
				display: 'flex',
				justifyContent: 'center',
				alignItems: 'center',
			}}
		>
			<Card style={{ width: 360 }}>
				<Title level={3} style={{ textAlign: 'center' }}>
					Admin Login
				</Title>
				<Form layout='vertical' onFinish={onFinish}>
					<Form.Item name='login' label='Логин' rules={[{ required: true }]}>
						<Input />
					</Form.Item>
					<Form.Item
						name='password'
						label='Пароль'
						rules={[{ required: true }]}
					>
						<Input.Password />
					</Form.Item>
					<Button type='primary' htmlType='submit' block loading={loading}>
						Войти
					</Button>
				</Form>
			</Card>
		</div>
	)
}
