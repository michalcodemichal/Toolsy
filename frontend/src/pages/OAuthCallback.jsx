import React, { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import Loading from '../components/Loading'

const OAuthCallback = () => {
  const [searchParams] = useSearchParams()
  const { setAuth } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const token = searchParams.get('token')
    const userId = searchParams.get('userId')
    const username = searchParams.get('username')
    const email = searchParams.get('email')
    const firstName = searchParams.get('firstName')
    const lastName = searchParams.get('lastName')
    const error = searchParams.get('error')

    if (error) {
      toast.error('Błąd logowania przez Google')
      navigate('/login')
      return
    }

    if (token && userId) {
      localStorage.setItem('token', token)
      localStorage.setItem(
        'user',
        JSON.stringify({
          id: userId,
          username: username,
          email: email,
          firstName: firstName,
          lastName: lastName,
        })
      )

      setAuth({
        user: {
          id: userId,
          username: username,
          email: email,
          firstName: firstName,
          lastName: lastName,
        },
        token: token,
      })

      toast.success('Zalogowano przez Google pomyślnie!')
      navigate('/dashboard')
    } else {
      toast.error('Brak danych autoryzacji')
      navigate('/login')
    }
  }, [searchParams, navigate, setAuth])

  return <Loading />
}

export default OAuthCallback

