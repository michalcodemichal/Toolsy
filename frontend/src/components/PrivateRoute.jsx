import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const PrivateRoute = ({ children, adminOnly = false }) => {
  const { user, loading } = useAuth()

  if (loading) {
    return <div>Ładowanie...</div>
  }

  if (!user) {
    return <Navigate to="/login" replace />
  }

  if (adminOnly && !user.roles?.includes('ADMIN')) {
    return <Navigate to="/dashboard" replace />
  }

  return children
}

export default PrivateRoute



