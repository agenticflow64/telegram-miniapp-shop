import { PhotoReviewRequest, ReviewDTO, ReviewResponse } from '../types/review'
import api from '../utils/api'

export const ReviewService = {
	create: (article: number, data: ReviewDTO) =>
		api
			.post<ReviewResponse>(`/review?article=${article}`, data)
			.then(res => res.data),

	uploadPhoto: (data: PhotoReviewRequest, file: File) => {
		const formData = new FormData()
		formData.append('file', file)
		formData.append('reviewId', data.review_id.toString())
		formData.append('indexNumber', data.index_number.toString())
		return api
			.post<string>('/review/photo', formData, {
				headers: { 'Content-Type': 'multipart/form-data' },
			})
			.then(res => res.data)
	},
}
