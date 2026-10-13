import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { CategoryService } from '../api/category'
import type {
	CategoryAttributesResponse,
	CategoryResponse,
} from '../types/category'

export const useCategoryFilters = () => {
	const [searchParams] = useSearchParams()
	const initialCategory = Number(searchParams.get('categoryId')) || null

	const [selectedCategory, setSelectedCategory] = useState<number | null>(
		initialCategory
	)
	const [selectedFilters, setSelectedFilters] = useState<
		Record<number, string[]>
	>(() => {
		const filters: Record<number, string[]> = {}
		searchParams.forEach((value, key) => {
			const match = key.match(/attributes\[(\d+)\]/)
			if (match) {
				const attrId = parseInt(match[1], 10)
				filters[attrId] = [...(filters[attrId] || []), value]
			}
		})
		return filters
	})

	const { data: categories } = useQuery<CategoryResponse>({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})

	const { data: attributes, isLoading } = useQuery<CategoryAttributesResponse>({
		queryKey: ['categoryAttributes', selectedCategory],
		queryFn: () => {
			if (!selectedCategory) throw new Error('Category not selected')
			return CategoryService.getAttributes(selectedCategory)
		},
		enabled: !!selectedCategory,
	})

	const handleCategoryChange = (categoryId: number) => {
		setSelectedCategory(categoryId)
		setSelectedFilters({})
	}

	const handleFilterChange = (attributeId: number, values: string[]) => {
		setSelectedFilters(prev => ({
			...prev,
			[attributeId]: values,
		}))
	}

	const resetFilters = () => {
		setSelectedCategory(null)
		setSelectedFilters({})
	}

	return {
		categories: categories?.categories
			? Object.entries(categories.categories)
			: [],
		attributes: attributes?.attributes || [],
		selectedCategory,
		selectedFilters,
		isLoading,
		handleCategoryChange,
		handleFilterChange,
		resetFilters,
	}
}
