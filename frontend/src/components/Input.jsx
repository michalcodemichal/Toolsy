import React from 'react'

const Input = ({ label, type = 'text', value, onChange, error, placeholder, required = false, className = '', ...props }) => {
  const isTextarea = type === 'textarea'
  const inputType = isTextarea ? undefined : type

  const baseInputClasses = "w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 h-10 bg-white/80 backdrop-blur-sm hover:border-gray-300 focus:bg-white shadow-sm focus:shadow-md"
  const errorClasses = error ? "border-red-400 focus:ring-red-400 focus:border-red-500" : ""

  return (
    <div className={`${className.includes('mb-0') ? '' : 'mb-4'} ${className}`}>
      {label && (
        <label className="block text-sm font-medium text-gray-700 mb-1">
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
      {error && <span className="text-red-500 text-sm mt-1 block">{error}</span>}
    </div>
  )
}

export default Input

