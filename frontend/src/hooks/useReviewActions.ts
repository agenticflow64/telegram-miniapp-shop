import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ReviewService } from '../api'
import { ReviewDTO } from '../types/review'

export const useReviewActions = () => {
	const queryClient = useQueryClient()

	const createReview = useMutation({
		mutationFn: (data: { article: number; review: ReviewDTO }) =>
			ReviewService.create(data.article, data.review),
		onSuccess: (_, vars) => {
			queryClient.invalidateQueries({
				queryKey: ['reviews', vars.article],
			})
			queryClient.invalidateQueries({
				queryKey: ['product', vars.article],
			})
		},
	})

	const uploadPhoto = useMutation({
		mutationFn: (data: { reviewId: number; index: number; file: File }) =>
			ReviewService.uploadPhoto(
				{ review_id: data.reviewId, index_number: data.index },
				data.file
			),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['reviews'] })
		},
	})

	const uploadReviewPhoto = useMutation({
		mutationFn: (data: { reviewId: number; index: number; file: File }) => {
			const formData = new FormData()
			formData.append('file', data.file)
			return ReviewService.uploadPhoto(
				{
					review_id: data.reviewId,
					index_number: data.index,
				},
				data.file
			)
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['product'] })
		},
	})

	return { createReview, uploadPhoto, uploadReviewPhoto }
}
