import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import Input from '../components/Input'
import Button from '../components/Button'
import Card from '../components/Card'
import './Auth.css'

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phoneNumber: ''
  })
  const [loading, setLoading] = useState(false)
  const [hashedPassword, setHashedPassword] = useState('')
  const [passwordError, setPasswordError] = useState('')
  const { register } = useAuth()
  const navigate = useNavigate()

  const validatePassword = (password) => {
    if (password.length < 6) {
      return 'Hasło musi zawierać więcej niż 6 znaków'
    }
    if (!/[A-Z]/.test(password)) {
      return 'Hasło musi zawierać co najmniej jedną wielką literę'
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
      return 'Hasło musi zawierać co najmniej jeden znak specjalny'
    }
    return ''
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: value })
    
    if (name === 'password') {
      const error = validatePassword(value)
      setPasswordError(error)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    const error = validatePassword(formData.password)
    if (error) {
      setPasswordError(error)
      toast.error(error)
      return
    }
    
    setLoading(true)

    try {
      const response = await register(formData)
      if (response.hashedPassword) {
        setHashedPassword(response.hashedPassword)
      }
      toast.success('Rejestracja pomyślna')
      navigate('/dashboard')
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd rejestracji')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <Card className="auth-card">
        <h1>Rejestracja</h1>
        <form onSubmit={handleSubmit}>
          <Input
            label="Nazwa użytkownika"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
            placeholder="Wprowadź nazwę użytkownika"
          />
          <Input
            label="Email"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            placeholder="Wprowadź email"
          />
          <Input
            label="Hasło"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            placeholder="Wprowadź hasło"
          />
          {passwordError && (
            <p className="text-red-500 text-sm mt-1">{passwordError}</p>
          )}
          {hashedPassword && (
            <div className="mt-4 p-3 bg-gray-100 rounded">
              <p className="text-sm font-semibold mb-1">Zahashowane hasło:</p>
              <p className="text-xs break-all text-gray-700">{hashedPassword}</p>
            </div>
          )}
          <Input
            label="Imię"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            required
            placeholder="Wprowadź imię"
          />
          <Input
            label="Nazwisko"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            required
            placeholder="Wprowadź nazwisko"
          />
          <Input
            label="Numer telefonu"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
            placeholder="Wprowadź numer telefonu"
          />
          <Button type="submit" disabled={loading} className="auth-button">
            {loading ? 'Rejestracja...' : 'Zarejestruj się'}
          </Button>
        </form>
        <p className="auth-link">
          Masz już konto? <Link to="/login">Zaloguj się</Link>
        </p>
      </Card>
    </div>
  )
}

export default Register



