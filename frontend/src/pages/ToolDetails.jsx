import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import { getToolById } from '../services/toolService'
import { createRental } from '../services/rentalService'
import Card from '../components/Card'
import Button from '../components/Button'
import Input from '../components/Input'
import Loading from '../components/Loading'
import './ToolDetails.css'

const ToolDetails = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [tool, setTool] = useState(null)
  const [loading, setLoading] = useState(true)
  const [renting, setRenting] = useState(false)
  const [showRentalForm, setShowRentalForm] = useState(false)
  const [rentalData, setRentalData] = useState({
    startDate: '',
    endDate: '',
    notes: ''
  })

  useEffect(() => {
    const fetchTool = async () => {
      try {
        const data = await getToolById(id)
        setTool(data)
      } catch (error) {
        toast.error('Błąd ładowania narzędzia')
        navigate('/tools')
      } finally {
        setLoading(false)
      }
    }

    fetchTool()
  }, [id, navigate])

  const handleRentalSubmit = async (e) => {
    e.preventDefault()
    if (!user) {
      toast.error('Musisz być zalogowany, aby wypożyczyć narzędzie')
      navigate('/login')
      return
    }

    setRenting(true)
    try {
      await createRental({
        toolId: parseInt(id),
        startDate: rentalData.startDate,
        endDate: rentalData.endDate,
        notes: rentalData.notes
      })
      toast.success('Wypożyczenie utworzone pomyślnie')
      setShowRentalForm(false)
      setRentalData({ startDate: '', endDate: '', notes: '' })
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd tworzenia wypożyczenia')
    } finally {
      setRenting(false)
    }
  }

  if (loading) {
    return <Loading />
  }

  if (!tool) {
    return null
  }

  return (
    <div className="tool-details-container">
      <div className="tool-details-content">
        <div className="tool-details-image">
          {tool.imageUrl ? (
            <img src={tool.imageUrl} alt={tool.name} />
          ) : (
            <div className="tool-details-placeholder">Brak zdjęcia</div>
          )}
        </div>
        <div className="tool-details-info">
          <h1>{tool.name}</h1>
          <p className="tool-details-category">{tool.category}</p>
          <p className="tool-details-description">{tool.description}</p>
          <div className="tool-details-meta">
            <div className="tool-details-price">
              <span className="price-label">Cena dzienna:</span>
              <span className="price-value">{tool.dailyPrice} zł</span>
            </div>
            <div className="tool-details-quantity">
              <span>Dostępna ilość: {tool.quantity}</span>
            </div>
            <div className={`tool-details-status tool-status-${tool.status.toLowerCase()}`}>
              {tool.status === 'AVAILABLE' && 'Dostępne'}
              {tool.status === 'RENTED' && 'Wypożyczone'}
              {tool.status === 'MAINTENANCE' && 'Konserwacja'}
              {tool.status === 'UNAVAILABLE' && 'Niedostępne'}
            </div>
          </div>
          {user && tool.status === 'AVAILABLE' && (
            <div className="tool-details-actions">
              {!showRentalForm ? (
                <Button onClick={() => setShowRentalForm(true)}>
                  Wypożycz narzędzie
                </Button>
              ) : (
                <Card className="rental-form-card">
                  <h3>Formularz wypożyczenia</h3>
                  <form onSubmit={handleRentalSubmit}>
                    <Input
                      label="Data rozpoczęcia"
                      type="date"
                      value={rentalData.startDate}
                      onChange={(e) =>
                        setRentalData({ ...rentalData, startDate: e.target.value })
                      }
                      required
                      min={new Date().toISOString().split('T')[0]}
                    />
                    <Input
                      label="Data zakończenia"
                      type="date"
                      value={rentalData.endDate}
                      onChange={(e) =>
                        setRentalData({ ...rentalData, endDate: e.target.value })
                      }
                      required
                      min={rentalData.startDate || new Date().toISOString().split('T')[0]}
                    />
                    <Input
                      label="Uwagi (opcjonalnie)"
                      type="textarea"
                      value={rentalData.notes}
                      onChange={(e) =>
                        setRentalData({ ...rentalData, notes: e.target.value })
                      }
                    />
                    <div className="rental-form-actions">
                      <Button type="submit" disabled={renting}>
                        {renting ? 'Tworzenie...' : 'Potwierdź wypożyczenie'}
                      </Button>
                      <Button
                        type="button"
                        variant="secondary"
                        onClick={() => {
                          setShowRentalForm(false)
                          setRentalData({ startDate: '', endDate: '', notes: '' })
                        }}
                      >
                        Anuluj
                      </Button>
                    </div>
                  </form>
                </Card>
              )}
            </div>
          )}
          {!user && (
            <p className="login-prompt">
              <a href="/login">Zaloguj się</a>, aby wypożyczyć narzędzie
            </p>
          )}
        </div>
      </div>
    </div>
  )
}

export default ToolDetails

