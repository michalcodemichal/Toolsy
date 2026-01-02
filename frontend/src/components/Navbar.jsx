import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './Navbar.css'

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-slate-800 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/" className="text-2xl font-bold text-white hover:text-blue-300 transition-colors">
            Toolsy
          </Link>
          <div className="flex items-center gap-4">
            <Link to="/tools" className="text-white hover:text-blue-300 px-3 py-2 rounded-md text-sm font-medium transition-colors">
              Narzędzia
            </Link>
            {user ? (
              <>
                <Link to="/dashboard" className="text-white hover:text-blue-300 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                  Panel
                </Link>
                <Link to="/my-rentals" className="text-white hover:text-blue-300 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                  Moje wypożyczenia
                </Link>
                {isAdmin() && (
                  <Link to="/admin" className="text-white hover:text-blue-300 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                    Admin
                  </Link>
                )}
                <span className="text-gray-300 px-3 py-2 text-sm">
                  {user.firstName} {user.lastName}
                </span>
                <button 
                  onClick={handleLogout} 
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                >
                  Wyloguj
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-white hover:text-blue-300 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                  Zaloguj
                </Link>
                <Link to="/register" className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors">
                  Rejestracja
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}

export default Navbar



