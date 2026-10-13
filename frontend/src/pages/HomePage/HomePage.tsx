import { ProductListContainer } from '../../containers/ProductListContainer/ProductListContainer'
import { SearchContainer } from '../../containers/SearchContainer/SearchContainer'
import { SortContainer } from '../../containers/SortContainer/SortContainer'
import styles from './HomePage.module.scss'

export const HomePage = () => {
	return (
		<div className={styles.container}>
			<SearchContainer />
			<div className={styles.toolbar}>
				<SortContainer />
			</div>
			<ProductListContainer />
		</div>
	)
}
