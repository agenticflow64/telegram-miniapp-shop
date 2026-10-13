import { DownOutlined } from '@ant-design/icons'
import type { MenuProps } from 'antd'
import { Button, Dropdown } from 'antd'
import styles from './SortControl.module.scss'

export type SortOption = 'price_asc' | 'price_desc' | 'rating' | 'orders'

type SortControlProps = {
	currentSort: SortOption
	onSortChange: (value: SortOption) => void
}

const labels: Record<SortOption, string> = {
	price_asc: 'Сначала дешевые',
	price_desc: 'Сначала дорогие',
	rating: 'По рейтингу',
	orders: 'По количеству заказов',
}

export const SortControl = ({
	currentSort,
	onSortChange,
}: SortControlProps) => {
	const items: MenuProps['items'] = Object.entries(labels).map(
		([key, label]) => ({
			key,
			label: <span className={styles.menuItem}>{label}</span>,
		})
	)

	return (
		<Dropdown
			menu={{
				items,
				selectable: true,
				defaultSelectedKeys: [currentSort],
				onClick: ({ key }) => onSortChange(key as SortOption),
			}}
			trigger={['click']}
		>
			<Button className={styles.sortButton}>
				{labels[currentSort]}
				<DownOutlined className={styles.icon} />
			</Button>
		</Dropdown>
	)
}
