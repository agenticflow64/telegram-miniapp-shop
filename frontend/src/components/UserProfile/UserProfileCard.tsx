// src/components/UserProfileCard/UserProfileCard.tsx
import { Avatar, Card, Typography } from 'antd'
import { TelegramUser } from '../../types/auth'
import styles from './UserProfileCard.module.scss'

const { Text, Title } = Typography

type UserProfileCardProps = {
	user: TelegramUser
}

export const UserProfileCard = ({ user }: UserProfileCardProps) => {
	return (
		<Card className={styles.card}>
			<div className={styles.profileHeader}>
				<Avatar src={user.photo_url} size={64} className={styles.avatar} />
				<div className={styles.userInfo}>
					<Title level={4} className={styles.username}>
						{user.first_name} {user.last_name}
					</Title>
					{user.username && (
						<Text type='secondary' className={styles.handle}>
							@{user.username}
						</Text>
					)}
				</div>
			</div>
		</Card>
	)
}
