import {
	ProductDocument,
	ProductPageResponse,
	ProductResponse,
} from '../types/product'
import api from './axios'

/* Маппер ES-документа → ProductResponse */
const mapDoc = (d: ProductDocument): ProductResponse => ({
	id: d.id,
	article_number: d.articleNumber,
	name: d.name,
	price: d.price ?? 0,
	rating: d.rating ?? 0,
	number_of_orders: d.numberOfOrders ?? 0,
	category_id: d.categoryId ?? 0,
	category_name: d.category ?? '',
	attributes:
		d.values?.map(v => ({
			attribute_id: v.id ?? 0,
			attribute_name: '',
			value: v.value ?? '',
		})) ?? [],
	photos: d.photos?.map(p => p.photo_url) ?? [],
})

export const ProductService = {
	/**
	 * Поиск в Elasticsearch.
	 *   backend: GET /products/search/elastic?query=<q>&page=<n>&size=<s>
	 */
	search: (q: string, page = 0, size = 20): Promise<ProductPageResponse> =>
		api
			.get<{
				content: ProductDocument[]
				pageable: { pageNumber: number }
				totalPages: number
				totalElements: number
			}>('/products/search/elastic', {
				params: { query: q, page, size },
			})
			.then(({ data }) => ({
				products: data.content.map(mapDoc),
				current_page: data.pageable.pageNumber,
				total_pages: data.totalPages,
				total_products: data.totalElements,
			})),
}
