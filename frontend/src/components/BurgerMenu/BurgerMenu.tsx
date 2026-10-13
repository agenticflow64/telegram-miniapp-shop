import { ControlOutlined } from '@ant-design/icons'
import styles from './BurgerMenu.module.scss'

type BurgerMenuProps = {
	onClick: () => void
}

export const BurgerMenu = ({ onClick }: BurgerMenuProps) => {
	return (
		<button className={styles.burgerButton} onClick={onClick}>
			<ControlOutlined
				style={{ transform: 'rotate(90deg)' }}
				className={styles.icon}
			/>
		</button>
	)
}
