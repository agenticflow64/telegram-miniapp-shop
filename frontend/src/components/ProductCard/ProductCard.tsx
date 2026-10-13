import {
	HeartFilled,
	HeartOutlined,
	ShoppingCartOutlined,
} from '@ant-design/icons'
import { Button, Rate, Space } from 'antd'
import { Link } from 'react-router-dom'
import type { ProductResponse } from '../../types/product'
import styles from './ProductCard.module.scss'

type ProductCardProps = {
	product: ProductResponse
	onCartAction: (product: ProductResponse, action: 'add' | 'remove') => void
	onWishlistAction: (article: number) => void
	isInWishlist?: boolean
	cartQuantity?: number
}

export const ProductCard = ({
	product,
	onCartAction,
	onWishlistAction,
	isInWishlist = false,
	cartQuantity = 0,
}: ProductCardProps) => {
	return (
		<div className={styles.card}>
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

				<div className={styles.meta}>
					<Rate
						value={product.rating}
						disabled
						style={{
							fontSize: '0.875em',
							gap: '0px',
							margin: '0 -1px',
						}}
					/>
					<span className={styles.price}>{product.price} ₽</span>
				</div>

				<div className={styles.actions}>
					{cartQuantity > 0 ? (
						<Space className={styles.quantityControl}>
							<Button
								type='primary'
								danger
								onClick={() => onCartAction(product, 'remove')}
							>
								-
							</Button>
							<span className={styles.quantity}>{cartQuantity}</span>
							<Button
								type='primary'
								onClick={() => onCartAction(product, 'add')}
							>
								+
							</Button>
						</Space>
					) : (
						<Button
							type='primary'
							icon={<ShoppingCartOutlined />}
							onClick={() => onCartAction(product, 'add')}
						>
							В корзину
						</Button>
					)}

					<Button
						type={isInWishlist ? 'primary' : 'default'}
						icon={isInWishlist ? <HeartFilled /> : <HeartOutlined />}
						onClick={() => onWishlistAction(product.article_number)}
					/>
				</div>
			</div>
		</div>
	)
}
