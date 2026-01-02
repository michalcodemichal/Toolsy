import React from 'react'

const Input = ({ label, type = 'text', value, onChange, error, placeholder, required = false, className = '', ...props }) => {
  const isTextarea = type === 'textarea'
  const inputType = isTextarea ? undefined : type

  const baseInputClasses = "w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors h-10"
  const errorClasses = error ? "border-red-500 focus:ring-red-500" : ""

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

