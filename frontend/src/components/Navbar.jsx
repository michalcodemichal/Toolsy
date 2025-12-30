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
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          Toolsy
        </Link>
        <div className="navbar-menu">
          <Link to="/tools" className="navbar-link">
            Narzędzia
          </Link>
          {user ? (
            <>
              <Link to="/dashboard" className="navbar-link">
                Panel
              </Link>
              <Link to="/my-rentals" className="navbar-link">
                Moje wypożyczenia
              </Link>
              {isAdmin() && (
                <Link to="/admin" className="navbar-link">
                  Admin
                </Link>
              )}
              <span className="navbar-user">
                {user.firstName} {user.lastName}
              </span>
              <button onClick={handleLogout} className="navbar-button">
                Wyloguj
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-link">
                Zaloguj
              </Link>
              <Link to="/register" className="navbar-button">
                Rejestracja
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}

export default Navbar

