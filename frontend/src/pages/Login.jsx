import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import Input from '../components/Input'
import Button from '../components/Button'
import Card from '../components/Card'
import './Auth.css'

const Login = () => {
  const [formData, setFormData] = useState({ username: '', password: '' })
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      await login(formData.username, formData.password)
      toast.success('Zalogowano pomyślnie')
      navigate('/dashboard')
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd logowania')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <Card className="auth-card">
        <h1>Logowanie</h1>
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
            label="Hasło"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            placeholder="Wprowadź hasło"
          />
          <Button type="submit" disabled={loading} className="auth-button">
            {loading ? 'Logowanie...' : 'Zaloguj'}
          </Button>
        </form>
        <p className="auth-link">
          Nie masz konta? <Link to="/register">Zarejestruj się</Link>
        </p>
      </Card>
    </div>
  )
}

export default Login



