import { createContext, useContext } from 'react'

export type AuthStatus = 'loading' | 'unauthenticated' | 'authenticated'

export interface AuthContextValue {
	status: AuthStatus
	token?: string
	login: (login: string, password: string) => Promise<void>
	logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)

export const useAuth = () => {
	const ctx = useContext(AuthContext)
	if (!ctx) throw new Error('useAuth must be used inside <AuthProvider>')
	return ctx
}
