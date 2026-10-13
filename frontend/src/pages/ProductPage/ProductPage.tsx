import {
	ArrowLeftOutlined,
	HeartFilled,
	HeartOutlined,
	ShoppingCartOutlined,
} from '@ant-design/icons'
import { Button, Carousel, Rate, Space, Spin, message } from 'antd'
import { useNavigate, useParams } from 'react-router-dom'
import { useCartActions } from '../../hooks/useCart'
import { useProductDetails } from '../../hooks/useProducts'
import { useProductStatus } from '../../hooks/useProductStatus'
import { useReviewActions } from '../../hooks/useReviewActions'
import { useTelegramUser } from '../../hooks/useTelegramAuth'
import { useUserOrders } from '../../hooks/useUserOrders'
import { useWishlistActions } from '../../hooks/useWishList'
import { ReviewSection } from './components/ReviewSection'
import styles from './ProductPage.module.scss'

export const ProductPage = () => {
	const { createReview, uploadReviewPhoto } = useReviewActions()
	const { data: orders } = useUserOrders()

	//
	const { article } = useParams<{ article: string }>()
	const navigate = useNavigate()
	const { data: product, isLoading, error } = useProductDetails(Number(article))
	const { cartMap, wishlistSet } = useProductStatus()
	const { addToCart, removeFromCart } = useCartActions()
	const { addToWishlist, removeFromWishlist } = useWishlistActions()
	const user = useTelegramUser()
	if (!product) return <div>Товар не найден</div>
	const enrichedProduct = product
		? {
				...product,
				inCart: cartMap.get(product.article_number) || 0,
				inWishlist: wishlistSet.has(product.article_number),
		  }
		: null

	const handleCartAction = (action: 'add' | 'remove') => {
		if (!enrichedProduct) return

		if (action === 'add') {
			addToCart.mutate({
				article_number: enrichedProduct.article_number,
				quantity: 1,
			})
			message.success('Товар добавлен в корзину')
		} else {
			if (enrichedProduct.inCart === 1) {
				removeFromCart.mutate(enrichedProduct.article_number)
			} else {
				addToCart.mutate({
					article_number: enrichedProduct.article_number,
					quantity: -1,
				})
			}
			message.success('Количество товара изменено')
		}
	}

	if (isLoading) return <Spin className={styles.spinner} />
	if (error)
		return <div className={styles.error}>Ошибка загрузки: {error.message}</div>
	if (!enrichedProduct) return null

	const canReview =
		orders?.orders?.some(order =>
			order.items.some(item => item.article_number === product.article_number)
		) && !product.reviews?.some(review => review?.telegram_id === user?.id)

	const handleReviewSubmit = async (values: {
		rating: number
		text: string
		photos: File[]
	}) => {
		const review = await createReview.mutateAsync({
			article: product.article_number,
			review: {
				rating: values.rating,
				text: values.text,
			},
		})

		if (values.photos?.length) {
			await Promise.all(
				values.photos.map((file, i) =>
					uploadReviewPhoto.mutateAsync({
						reviewId: review.id,
						index: i + 1,
						file,
					})
				)
			)
		}
	}
	return (
		<div className={styles.container}>
			<Button
				type='link'
				icon={<ArrowLeftOutlined />}
				onClick={() => navigate(-1)}
				className={styles.backButton}
			>
				Назад
			</Button>

			<Carousel arrows autoplay className={styles.carousel}>
				{enrichedProduct.photos.map((photo, index) => (
					<div key={index}>
						<img src={photo} alt={`${enrichedProduct.name} ${index}`} />
					</div>
				))}
			</Carousel>

			<div className={styles.content}>
				<h1 className={styles.title}>{enrichedProduct.name}</h1>

				<div className={styles.meta}>
					<Rate
						value={enrichedProduct.rating}
						disabled
						className={styles.rating}
					/>
					<span className={styles.price}>{enrichedProduct.price} ₽</span>
				</div>

				<div className={styles.actions}>
					{enrichedProduct.inCart > 0 ? (
						<Space className={styles.quantityControl}>
							<Button
								type='primary'
								danger
								onClick={() => handleCartAction('remove')}
							>
								-
							</Button>
							<span className={styles.quantity}>{enrichedProduct.inCart}</span>
							<Button type='primary' onClick={() => handleCartAction('add')}>
								+
							</Button>
						</Space>
					) : (
						<Button
							type='primary'
							icon={<ShoppingCartOutlined />}
							onClick={() => handleCartAction('add')}
							className={styles.cartButton}
						>
							Добавить в корзину
						</Button>
					)}

					<Button
						type={enrichedProduct.inWishlist ? 'primary' : 'default'}
						icon={
							enrichedProduct.inWishlist ? <HeartFilled /> : <HeartOutlined />
						}
						onClick={() =>
							enrichedProduct.inWishlist
								? removeFromWishlist.mutate(enrichedProduct.article_number)
								: addToWishlist.mutate(enrichedProduct.article_number)
						}
					/>
				</div>

				{product.attributes?.length > 0 && (
					<div className={styles.attributes}>
						<h3>Характеристики:</h3>
						{product.attributes.map(attr => (
							<div key={attr.attribute_id} className={styles.attribute}>
								<span>{attr.attribute_name}:</span>
								<span>{attr.value}</span>
							</div>
						))}
					</div>
				)}
			</div>
			<ReviewSection
				reviews={product.reviews}
				productId={product.article_number}
				canReview={canReview || false}
				onReviewSubmit={handleReviewSubmit}
			/>
		</div>
	)
}
