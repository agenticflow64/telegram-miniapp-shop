import { Rate, Space, Typography } from 'antd'
import styles from './ReviewItem.module.scss'

const { Text } = Typography

export const ReviewItem = ({
	rating,
	text,
	photos,
	createdAt,
}: {
	rating: number
	text: string
	photos?: string[]
	createdAt: string
}) => {
	return (
		<div className={styles.reviewItem}>
			<Space size='middle' align='start'>
				<div className={styles.content}>
					<div className={styles.header}>
						<Rate disabled value={rating} className={styles.rating} />
						<Text type='secondary'>
							{new Date(createdAt).toLocaleDateString()}
						</Text>
					</div>

					<Text className={styles.text}>{text}</Text>

					{photos?.length ? (
						<div className={styles.photos}>
							{photos.map((photo, i) => (
								<img key={i} src={photo} alt='' className={styles.photo} />
							))}
						</div>
					) : null}
				</div>
			</Space>
		</div>
	)
}
