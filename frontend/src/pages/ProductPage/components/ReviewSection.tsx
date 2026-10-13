import { Button, Divider, Empty, Space, Typography } from 'antd'
import { useState } from 'react'
import { ReviewForm } from '../../../components/ReviewForm/ReviewForm'
import { ReviewItem } from '../../../components/ReviewItem/ReviewItem'
import type { ReviewDTO } from '../../../types/review'
import styles from './ReviewSection.module.scss'

const { Title, Text } = Typography

type ReviewSectionProps = {
	reviews: ReviewDTO[]
	productId: number
	canReview: boolean
	onReviewSubmit: (values: {
		rating: number
		text: string
		photos: File[]
	}) => Promise<void>
}

export const ReviewSection = ({
	reviews,
	canReview,
	onReviewSubmit,
}: ReviewSectionProps) => {
	const [formVisible, setFormVisible] = useState(false)
	const [loading, setLoading] = useState(false)

	const handleSubmit = async (values: {
		rating: number
		text: string
		photos: File[]
	}) => {
		setLoading(true)
		try {
			await onReviewSubmit({ ...values })
			setFormVisible(false)
		} finally {
			setLoading(false)
		}
	}

	return (
		<section className={styles.reviewSection}>
			<Divider />

			<Space size='large' align='center' className={styles.header}>
				<Title level={4}>Отзывы ({reviews.length})</Title>
				{canReview && (
					<Button type='primary' onClick={() => setFormVisible(true)}>
						Написать отзыв
					</Button>
				)}
			</Space>

			{reviews.length ? (
				<div className={styles.reviewsList}>
					{reviews.map(review => (
						<ReviewItem
							key={review.id}
							rating={review.rating}
							text={review.text}
							photos={review.photos}
							createdAt={review.created_at || ' '}
						/>
					))}
				</div>
			) : (
				<div className={styles.emptyReviews}>
					<Empty
						image={Empty.PRESENTED_IMAGE_SIMPLE}
						description={<Text type='secondary'>Пока нет отзывов</Text>}
					/>
				</div>
			)}

			<ReviewForm
				visible={formVisible}
				onCancel={() => setFormVisible(false)}
				onSubmit={handleSubmit}
				loading={loading}
			/>
		</section>
	)
}
