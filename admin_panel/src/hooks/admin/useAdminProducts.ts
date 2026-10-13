import { useMutation } from '@tanstack/react-query'
import { AdminProductApi } from '../../api/admin/product'
import {
	PhotoUploadRequestDTO,
	ProductCreateRequestDTO,
} from '../../types/product'
export const useCreateProduct = () =>
	useMutation<number, Error, ProductCreateRequestDTO>({
		mutationFn: AdminProductApi.create,
	})

export const useUploadPhoto = () =>
	useMutation<string, Error, { meta: PhotoUploadRequestDTO; file: File }>({
		mutationFn: ({ meta, file }) => AdminProductApi.uploadPhoto(meta, file),
	})
