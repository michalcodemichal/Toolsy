import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useDarkMode } from '../context/DarkModeContext'
import './Navbar.css'

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth()
  const { isDark, toggleDarkMode } = useDarkMode()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="gradient-secondary dark:from-slate-800 dark:to-slate-900 text-white shadow-2xl backdrop-blur-md border-b border-white/10 dark:border-gray-700">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-20">
          <Link to="/" className="text-3xl font-extrabold text-white hover:text-blue-200 dark:hover:text-blue-300 transition-all duration-300 hover:scale-105 flex items-center gap-2">
            <span className="text-4xl">🔧</span>
            <span className="bg-gradient-to-r from-blue-200 to-indigo-200 dark:from-blue-300 dark:to-indigo-300 bg-clip-text text-transparent">Toolsy</span>
          </Link>
          <div className="flex items-center gap-2">
            <button
              onClick={toggleDarkMode}
              className="text-white/90 hover:text-white px-3 py-2 rounded-lg text-lg transition-all duration-200 hover:bg-white/10 hover:scale-105"
              title={isDark ? 'Przełącz na tryb jasny' : 'Przełącz na tryb ciemny'}
            >
              {isDark ? '☀️' : '🌙'}
            </button>
            <Link to="/tools" className="text-white/90 hover:text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 hover:bg-white/10 hover:scale-105">
              Narzędzia
            </Link>
            {user ? (
              <>
                <Link to="/dashboard" className="text-white/90 hover:text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 hover:bg-white/10 hover:scale-105">
                  Panel
                </Link>
                <Link to="/my-rentals" className="text-white/90 hover:text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 hover:bg-white/10 hover:scale-105">
                  Moje wypożyczenia
                </Link>
                {isAdmin() && (
                  <Link to="/admin" className="text-white/90 hover:text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 hover:bg-white/10 hover:scale-105 bg-yellow-500/20 dark:bg-yellow-600/30 border border-yellow-400/30 dark:border-yellow-500/40">
                    Admin
                  </Link>
                )}
                <span className="text-white/80 px-4 py-2 text-sm font-medium bg-white/5 dark:bg-white/10 rounded-lg border border-white/10 dark:border-gray-700">
                  👤 {user.firstName} {user.lastName}
                </span>
                <button 
                  onClick={handleLogout} 
                  className="bg-gradient-to-r from-red-500 to-pink-500 hover:from-red-600 hover:to-pink-600 dark:from-red-600 dark:to-pink-600 dark:hover:from-red-700 dark:hover:to-pink-700 text-white px-5 py-2.5 rounded-lg text-sm font-semibold transition-all duration-200 shadow-lg shadow-red-500/30 hover:shadow-xl hover:shadow-red-500/50 hover:scale-105"
                >
                  Wyloguj
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-white/90 hover:text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 hover:bg-white/10 hover:scale-105">
                  Zaloguj
                </Link>
                <Link to="/register" className="gradient-primary text-white px-5 py-2.5 rounded-lg text-sm font-semibold transition-all duration-200 shadow-lg shadow-blue-500/50 hover:shadow-xl hover:shadow-blue-500/70 hover:scale-105">
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



