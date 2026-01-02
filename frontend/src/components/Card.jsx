import React from 'react'

const Card = ({ children, className = '', onClick, variant = 'default' }) => {
  const baseClasses = 'glass-effect rounded-2xl shadow-xl p-6 transition-all duration-300 border border-gray-200/50'
  const clickableClasses = onClick ? 'cursor-pointer card-hover' : ''
  
  const variantClasses = {
    default: 'bg-white/90',
    gradient: 'bg-gradient-to-br from-blue-50/90 to-indigo-50/90',
    elevated: 'bg-white shadow-2xl'
  }
  
  return (
    <div className={`${baseClasses} ${clickableClasses} ${variantClasses[variant] || variantClasses.default} ${className}`} onClick={onClick}>
      {children}
    </div>
  )
}

export default Card



