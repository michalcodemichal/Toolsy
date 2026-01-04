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
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-10">
        <div className="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 dark:from-blue-700 dark:via-indigo-700 dark:to-purple-700 rounded-3xl p-8 text-white shadow-2xl mb-8">
          <h1 className="text-4xl font-extrabold mb-2">Moje wypożyczenia</h1>
          <p className="text-xl text-blue-100 dark:text-blue-200">Zarządzaj swoimi wypożyczeniami</p>
        </div>
      </div>

      {rentals.length > 0 ? (
        <div className="space-y-4">
          {rentals.map((rental) => (
            <Card key={rental.id} className="rental-card">
              <div className="rental-card-header">
                <h2 className="text-xl font-bold text-gray-900 dark:text-gray-100">{rental.toolName}</h2>
                <span className={`rental-status rental-status-${rental.status.toLowerCase()}`}>
                  {rental.status === 'PENDING' && '⏳ Oczekujące'}
                  {rental.status === 'ACTIVE' && '✅ Aktywne'}
                  {rental.status === 'COMPLETED' && '✓ Zakończone'}
                  {rental.status === 'CANCELLED' && '❌ Anulowane'}
                  {rental.status === 'OVERDUE' && '⚠️ Przeterminowane'}
                </span>
              </div>
              <div className="rental-card-body">
                <div className="rental-info-item">
                  <span className="info-label">Data rozpoczęcia:</span>
                  <span className="text-gray-900 dark:text-gray-100">{new Date(rental.startDate).toLocaleDateString('pl-PL')}</span>
                </div>
                <div className="rental-info-item">
                  <span className="info-label">Data zakończenia:</span>
                  <span className="text-gray-900 dark:text-gray-100">{new Date(rental.endDate).toLocaleDateString('pl-PL')}</span>
                </div>
                {rental.quantity && (
                  <div className="rental-info-item">
                    <span className="info-label">Ilość:</span>
                    <span className="text-gray-900 dark:text-gray-100">{rental.quantity}</span>
                  </div>
                )}
                <div className="rental-info-item">
                  <span className="info-label">Cena całkowita:</span>
                  <span className="rental-price">{rental.totalPrice} zł</span>
                </div>
                {rental.notes && (
                  <div className="rental-info-item">
                    <span className="info-label">Uwagi:</span>
                    <span className="text-gray-900 dark:text-gray-100">{rental.notes}</span>
                  </div>
                )}
                {rental.returnedAt && (
                  <div className="rental-info-item">
                    <span className="info-label">Zwrócono:</span>
                    <span className="text-gray-900 dark:text-gray-100">{new Date(rental.returnedAt).toLocaleString('pl-PL')}</span>
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
        <Card className="text-center py-16">
          <div className="text-6xl mb-4">📦</div>
          <p className="text-gray-500 dark:text-gray-400 text-lg">Brak wypożyczeń</p>
        </Card>
      )}
    </div>
  )
}

export default MyRentals






