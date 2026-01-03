import React from 'react'

const Button = ({ children, onClick, variant = 'primary', type = 'button', disabled = false, className = '' }) => {
  const baseClasses = 'px-6 py-3 rounded-xl font-semibold transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed transform hover:scale-105 active:scale-95'
  
  const variantClasses = {
    primary: 'gradient-primary text-white btn-glow hover:shadow-2xl',
    secondary: 'bg-gradient-to-r from-slate-200 to-slate-300 dark:from-slate-600 dark:to-slate-700 hover:from-slate-300 hover:to-slate-400 dark:hover:from-slate-500 dark:hover:to-slate-600 text-slate-800 dark:text-slate-100 shadow-lg',
    danger: 'bg-gradient-to-r from-red-500 to-pink-500 dark:from-red-600 dark:to-pink-600 hover:from-red-600 hover:to-pink-600 dark:hover:from-red-700 dark:hover:to-pink-700 text-white shadow-lg shadow-red-500/50 dark:shadow-red-600/30 hover:shadow-xl',
    success: 'bg-gradient-to-r from-green-500 to-emerald-500 dark:from-green-600 dark:to-emerald-600 hover:from-green-600 hover:to-emerald-600 dark:hover:from-green-700 dark:hover:to-emerald-700 text-white shadow-lg shadow-green-500/50 dark:shadow-green-600/30 hover:shadow-xl',
  }
  
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseClasses} ${variantClasses[variant] || variantClasses.primary} ${className}`}
    >
      {children}
    </button>
  )
}

export default Button



