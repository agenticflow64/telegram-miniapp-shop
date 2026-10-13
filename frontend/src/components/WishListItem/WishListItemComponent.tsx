import { HeartFilled } from '@ant-design/icons'
import { Button, Rate, Skeleton } from 'antd'
import { Link } from 'react-router-dom'
import { useProductDetails } from '../../hooks/useProducts'
import { useWishlistActions } from '../../hooks/useWishList'
import styles from './WishListItem.module.scss'

type WishListItemProps = {
	article: number
	onRemove: (article: number) => void
}

export const WishListItem = ({ article, onRemove }: WishListItemProps) => {
	const { data: product, isLoading } = useProductDetails(article)
	const { removeFromWishlist } = useWishlistActions()

	const handleRemove = () => {
		onRemove(article)
		removeFromWishlist.mutate(article)
	}

	if (!product && !isLoading) return null

	return (
		<div className={styles.item}>
			<Skeleton loading={isLoading} active>
				{product && (
					<>
						<Link
							to={`/product/${product.article_number}`}
							className={styles.imageLink}
						>
							<img
								src={product.photos[0] || '/placeholder-product.jpg'}
								alt={product.name}
								className={styles.image}
							/>
						</Link>
						<div className={styles.content}>
							<h3 className={styles.title}>{product.name}</h3>
							<Rate value={product.rating} disabled className={styles.rate} />
							<div className={styles.price}>{product.price} ₽</div>
						</div>

						<Button
							type='primary'
							danger
							icon={<HeartFilled />}
							onClick={handleRemove}
							className={styles.removeButton}
						/>
					</>
				)}
			</Skeleton>
		</div>
	)
}
