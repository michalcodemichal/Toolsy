import React from 'react'

const Input = ({ label, type = 'text', value, onChange, error, placeholder, required = false, className = '', ...props }) => {
  const isTextarea = type === 'textarea'
  const inputType = isTextarea ? undefined : type

  const baseInputClasses = "w-full px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 transition-all duration-200 h-10 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm hover:border-gray-300 dark:hover:border-gray-600 focus:bg-white dark:focus:bg-slate-800 shadow-sm focus:shadow-md text-gray-900 dark:text-gray-100 placeholder-gray-400 dark:placeholder-gray-500"
  const errorClasses = error ? "border-red-400 dark:border-red-600 focus:ring-red-400 dark:focus:ring-red-500 focus:border-red-500 dark:focus:border-red-600" : ""

  return (
    <div className={`${className.includes('mb-0') ? '' : 'mb-4'} ${className}`}>
      {label && (
        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}
      {isTextarea ? (
        <textarea
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className={`${baseInputClasses.replace('h-10', '')} ${errorClasses} min-h-[100px] resize-y`}
          {...props}
        />
      ) : (
        <input
          type={inputType}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className={`${baseInputClasses} ${errorClasses}`}
          {...props}
        />
      )}
      {error && <span className="text-red-500 dark:text-red-400 text-sm mt-1 block">{error}</span>}
    </div>
  )
}

export default Input

