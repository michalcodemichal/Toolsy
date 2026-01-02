import React from 'react'
import './Button.css'

const Button = ({ children, onClick, variant = 'primary', type = 'button', disabled = false, className = '' }) => {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`button button-${variant} ${className}`}
    >
      {children}
    </button>
  )
}

export default Button



