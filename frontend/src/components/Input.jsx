import React from 'react'
import './Input.css'

const Input = ({ label, type = 'text', value, onChange, error, placeholder, required = false, ...props }) => {
  const isTextarea = type === 'textarea'
  const inputType = isTextarea ? undefined : type

  return (
    <div className="input-group">
      {label && (
        <label className="input-label">
          {label}
          {required && <span className="required">*</span>}
        </label>
      )}
      {isTextarea ? (
        <textarea
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className={`input input-textarea ${error ? 'input-error' : ''}`}
          {...props}
        />
      ) : (
        <input
          type={inputType}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className={`input ${error ? 'input-error' : ''}`}
          {...props}
        />
      )}
      {error && <span className="input-error-message">{error}</span>}
    </div>
  )
}

export default Input

