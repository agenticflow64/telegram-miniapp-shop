import { useLocation } from 'react-router-dom'
import { Navbar } from '../../components/Navbar/Navbar'

export const NavbarContainer = () => {
	const location = useLocation()
	return <Navbar activePath={location.pathname} />
}
