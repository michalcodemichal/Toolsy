import React, { createContext, useState, useEffect, useContext } from 'react'
import { login as loginApi, register as registerApi } from '../services/authService'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const userData = localStorage.getItem('user')
    if (token && userData) {
      try {
        setUser(JSON.parse(userData))
      } catch (error) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
    }
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    try {
      const response = await loginApi(username, password)
      localStorage.setItem('token', response.token)
      const userData = {
        id: response.id,
        username: response.username,
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        roles: response.roles
      }
      localStorage.setItem('user', JSON.stringify(userData))
      setUser(userData)
      return response
    } catch (error) {
      throw error
    }
  }

  const register = async (userData) => {
    try {
      const response = await registerApi(userData)
      localStorage.setItem('token', response.token)
      const user = {
        id: response.id,
        username: response.username,
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        roles: response.roles
      }
      localStorage.setItem('user', JSON.stringify(user))
      setUser(user)
      return response
    } catch (error) {
      throw error
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  const isAdmin = () => {
    return user?.roles?.includes('ADMIN')
  }

  const value = {
    user,
    login,
    register,
    logout,
    isAdmin,
    loading
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}






