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
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      await register(formData)
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



