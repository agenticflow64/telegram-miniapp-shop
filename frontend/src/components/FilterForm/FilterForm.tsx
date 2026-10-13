import { Button, Select, Typography } from 'antd'
import type { CategoryAttributesResponse } from '../../types/category'
import styles from './FilterForm.module.scss'

const { Title } = Typography

type FilterFormProps = {
	categories: Array<[string, number]>
	attributes: CategoryAttributesResponse['attributes']
	selectedCategory: number | null
	selectedFilters: Record<number, string[]>
	onCategoryChange: (categoryId: number) => void
	onFilterChange: (attributeId: number, values: string[]) => void
	onApply: () => void
	onReset: () => void
}

export const FilterForm = ({
	categories,
	attributes,
	selectedCategory,
	selectedFilters,
	onCategoryChange,
	onFilterChange,
	onApply,
	onReset,
}: FilterFormProps) => {
	return (
		<div className={styles.filterForm}>
			<div className={styles.section}>
				<Title level={5} className={styles.title}>
					Категория
				</Title>
				<Select
					placeholder='Выберите категорию'
					options={categories.map(([name, id]) => ({
						label: name,
						value: id,
					}))}
					value={selectedCategory}
					onChange={onCategoryChange}
					style={{ width: '100%' }}
				/>
			</div>

			{selectedCategory && attributes.length > 0 && (
				<div className={styles.section}>
					{attributes.map(attr => (
						<div key={attr.id} className={styles.filterGroup}>
							<div className={styles.filterName}>{attr.name}</div>
							<Select
								mode='multiple'
								placeholder={`Выберите ${attr.name}`}
								value={selectedFilters[attr.id] || []}
								onChange={values => onFilterChange(attr.id, values)}
								options={attr.values.map(value => ({
									label: value,
									value: value,
								}))}
								style={{ width: '100%' }}
								dropdownMatchSelectWidth={false}
								popupClassName={styles.selectDropdown}
								showSearch
								filterOption={(input, option) =>
									(option?.label ?? '')
										.toLowerCase()
										.includes(input.toLowerCase())
								}
							/>
						</div>
					))}
				</div>
			)}

			<div className={styles.actions}>
				<Button onClick={onReset}>Сбросить</Button>
				<Button type='primary' onClick={onApply}>
					Показать товары
				</Button>
			</div>
		</div>
	)
}
