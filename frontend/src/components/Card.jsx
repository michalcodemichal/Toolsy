import React from 'react'

const Card = ({ children, className = '', onClick }) => {
  const baseClasses = 'bg-white rounded-lg shadow-md p-6 transition-shadow'
  const clickableClasses = onClick ? 'cursor-pointer hover:shadow-lg' : ''
  
  return (
    <div className={`${baseClasses} ${clickableClasses} ${className}`} onClick={onClick}>
      {children}
    </div>
  )
}

export default Card



