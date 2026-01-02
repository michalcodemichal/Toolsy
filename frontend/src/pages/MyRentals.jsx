import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { getMyRentals, cancelRental } from '../services/rentalService'
import Card from '../components/Card'
import Button from '../components/Button'
import Loading from '../components/Loading'
import './MyRentals.css'

const MyRentals = () => {
  const [rentals, setRentals] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchRentals()
  }, [])

  const fetchRentals = async () => {
    try {
      const data = await getMyRentals()
      setRentals(data)
    } catch (error) {
      toast.error('Błąd ładowania wypożyczeń')
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = async (id) => {
    if (!window.confirm('Czy na pewno chcesz anulować to wypożyczenie?')) {
      return
    }

    try {
      await cancelRental(id)
      toast.success('Wypożyczenie anulowane')
      fetchRentals()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd anulowania wypożyczenia')
    }
  }

  if (loading) {
    return <Loading />
  }

  return (
    <div className="my-rentals-container">
      <div className="my-rentals-header">
        <h1>Moje wypożyczenia</h1>
      </div>

      {rentals.length > 0 ? (
        <div className="rentals-list">
          {rentals.map((rental) => (
            <Card key={rental.id} className="rental-card">
              <div className="rental-card-header">
                <h2>{rental.toolName}</h2>
                <span className={`rental-status rental-status-${rental.status.toLowerCase()}`}>
                  {rental.status === 'PENDING' && 'Oczekujące'}
                  {rental.status === 'ACTIVE' && 'Aktywne'}
                  {rental.status === 'COMPLETED' && 'Zakończone'}
                  {rental.status === 'CANCELLED' && 'Anulowane'}
                  {rental.status === 'OVERDUE' && 'Przeterminowane'}
                </span>
              </div>
              <div className="rental-card-body">
                <div className="rental-info-item">
                  <span className="info-label">Data rozpoczęcia:</span>
                  <span>{new Date(rental.startDate).toLocaleDateString('pl-PL')}</span>
                </div>
                <div className="rental-info-item">
                  <span className="info-label">Data zakończenia:</span>
                  <span>{new Date(rental.endDate).toLocaleDateString('pl-PL')}</span>
                </div>
                <div className="rental-info-item">
                  <span className="info-label">Cena całkowita:</span>
                  <span className="rental-price">{rental.totalPrice} zł</span>
                </div>
                {rental.notes && (
                  <div className="rental-info-item">
                    <span className="info-label">Uwagi:</span>
                    <span>{rental.notes}</span>
                  </div>
                )}
                {rental.returnedAt && (
                  <div className="rental-info-item">
                    <span className="info-label">Zwrócono:</span>
                    <span>{new Date(rental.returnedAt).toLocaleString('pl-PL')}</span>
                  </div>
                )}
              </div>
              <div className="rental-card-actions">
                {(rental.status === 'PENDING' || rental.status === 'ACTIVE') && (
                  <Button
                    variant="danger"
                    onClick={() => handleCancel(rental.id)}
                  >
                    Anuluj wypożyczenie
                  </Button>
                )}
              </div>
            </Card>
          ))}
        </div>
      ) : (
        <div className="empty-message">
          <p>Brak wypożyczeń</p>
        </div>
      )}
    </div>
  )
}

export default MyRentals



