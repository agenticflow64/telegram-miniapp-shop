import { DeleteOutlined } from '@ant-design/icons'
import { Button, Skeleton } from 'antd'
import { Link } from 'react-router-dom'
import { useProductDetails } from '../../hooks/useProducts'
import styles from './CartItem.module.scss'

type CartItemProps = {
	article: number
	quantity: number
	onRemove: (article: number) => void
	onQuantityChange: (article: number, delta: number) => void
	editable?: boolean
}

export const CartItemComponent = ({
	article,
	quantity,
	onRemove,
	onQuantityChange,
	editable = true,
}: CartItemProps) => {
	const { data: product, isLoading } = useProductDetails(article)

	if (!product && !isLoading) return null

	return (
		<div className={styles.item}>
			<Skeleton loading={isLoading} active paragraph={{ rows: 3 }}>
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
							<div className={styles.price}>{product.price} ₽</div>

							<div className={styles.controls}>
								{editable ? (
									<div className={styles.quantityControl}>
										<Button
											shape='circle'
											onClick={() => onQuantityChange(article, -1)}
											disabled={quantity <= 1}
										>
											-
										</Button>
										<span className={styles.quantity}>{quantity}</span>
										<Button
											shape='circle'
											onClick={() => onQuantityChange(article, 1)}
										>
											+
										</Button>
									</div>
								) : (
									<div className={styles.quantityText}>
										Количество: {quantity}
									</div>
								)}
								<div className={styles.total}>
									Итого: {(product.price * quantity).toFixed(2)} ₽
								</div>
							</div>
						</div>

						{editable && (
							<Button
								type='text'
								danger
								icon={<DeleteOutlined />}
								onClick={() => onRemove(article)}
								className={styles.removeButton}
							/>
						)}
					</>
				)}
			</Skeleton>
		</div>
	)
}
