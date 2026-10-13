import { Modal, Spin } from 'antd'
import { useCallback, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { BurgerMenu } from '../../components/BurgerMenu/BurgerMenu'
import { FilterForm } from '../../components/FilterForm/FilterForm'
import { SearchBar } from '../../components/SearchBar/SearchBar'
import { useCategoryFilters } from '../../hooks/useCategoryFilters'
import styles from './SearchContainer.module.scss'

export const SearchContainer = () => {
	const [isModalOpen, setIsModalOpen] = useState(false)
	const [searchParams, setSearchParams] = useSearchParams()
	const {
		categories,
		attributes,
		selectedCategory,
		selectedFilters,
		isLoading,
		handleCategoryChange,
		handleFilterChange,
		resetFilters,
	} = useCategoryFilters()

	const handleSearch = useCallback(
		(value: string) => {
			setSearchParams(prev => {
				const next = new URLSearchParams(prev)
				if (value) {
					next.set('search', value)
				} else {
					next.delete('search')
				}
				next.set('page', '0')
				return next
			})
		},
		[setSearchParams]
	)

	const handleApplyFilters = () => {
		const newParams = new URLSearchParams()

		if (selectedCategory) {
			newParams.set('categoryId', selectedCategory.toString())
			newParams.set('page', '0')

			Object.entries(selectedFilters).forEach(([attrId, values]) => {
				values.forEach(value =>
					newParams.append(`attributes[${attrId}]`, value)
				)
			})
		}

		// Сохраняем параметры поиска при применении фильтров
		const searchQuery = searchParams.get('search')
		if (searchQuery) newParams.set('search', searchQuery)

		setSearchParams(newParams)
		setIsModalOpen(false)
	}

	const handleResetFilters = () => {
		resetFilters()
		setSearchParams(new URLSearchParams({ page: '0' }))
		setIsModalOpen(false)
	}

	return (
		<div className={styles.container}>
			<SearchBar
				initialValue={searchParams.get('search') || ''}
				onSearch={handleSearch}
			/>
			<BurgerMenu onClick={() => setIsModalOpen(true)} />

			<Modal
				title='Фильтры'
				open={isModalOpen}
				onCancel={() => setIsModalOpen(false)}
				footer={null}
				centered
				width='90%'
				style={{ top: '5vh', maxWidth: '1440px' }}
				bodyStyle={{ height: '85vh', overflowY: 'auto', padding: '16px 24px' }}
			>
				<div className={styles.modalContent}>
					{isLoading ? (
						<Spin />
					) : (
						<FilterForm
							categories={categories}
							attributes={attributes}
							selectedCategory={selectedCategory}
							selectedFilters={selectedFilters}
							onCategoryChange={handleCategoryChange}
							onFilterChange={handleFilterChange}
							onApply={handleApplyFilters}
							onReset={handleResetFilters}
						/>
					)}
				</div>
			</Modal>
		</div>
	)
}
