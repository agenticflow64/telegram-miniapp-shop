import { SearchOutlined } from '@ant-design/icons'
import { Input } from 'antd'
import { useEffect, useState } from 'react'
import { useDebounce } from '../../hooks/useDebounce'
import styles from './SearchBar.module.scss'

type SearchBarProps = {
	initialValue?: string
	onSearch: (value: string) => void
}

export const SearchBar = ({ initialValue = '', onSearch }: SearchBarProps) => {
	const [value, setValue] = useState(initialValue)

	useEffect(() => {
		setValue(initialValue)
	}, [initialValue])
	const debounced = useDebounce(value, 400)
	useEffect(() => {
		onSearch(debounced.trim())
	}, [debounced, onSearch])
	return (
		<Input
			placeholder='Поиск товаров…'
			prefix={<SearchOutlined className={styles.icon} />}
			className={styles.searchInput}
			allowClear
			value={value}
			onChange={e => setValue(e.target.value)}
			onPressEnter={e => onSearch((e.target as HTMLInputElement).value.trim())}
		/>
	)
}
