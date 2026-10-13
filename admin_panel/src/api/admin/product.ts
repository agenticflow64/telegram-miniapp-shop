import {
	PhotoUploadRequestDTO,
	ProductCreateRequestDTO,
} from '../../types/product'
import api from '../axios'

export const AdminProductApi = {
	/** создать товар, ответ — articleNumber (Long) */
	create: (body: ProductCreateRequestDTO) =>
		api.post<number>('/products/create', body).then(r => r.data),

	/** загрузить ОДНО фото */
	uploadPhoto: (body: PhotoUploadRequestDTO, file: File) => {
		const formData = new FormData()
		formData.append('file', file)
		formData.append('articleNumber', body.articleNumber.toString())
		formData.append('indexNumber', body.indexNumber.toString())

		/* ⬇️  .then(r => r.data)  — берём payload вместо AxiosResponse */
		return api
			.post<string>('/products/uploadPhoto', formData, {
				headers: { 'Content-Type': 'multipart/form-data' },
			})
			.then(r => r.data)
	},
	delete: (articleNumber: number) =>
		api.delete<void>(`/products/${articleNumber}`).then(r => r.data),
}
