import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { getMyRentals } from '../services/rentalService'
import { getAvailableTools } from '../services/toolService'
import Card from '../components/Card'
import Loading from '../components/Loading'
import './Dashboard.css'

const Dashboard = () => {
  const { user } = useAuth()
  const [rentals, setRentals] = useState([])
  const [tools, setTools] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [rentalsData, toolsData] = await Promise.all([
          getMyRentals(),
          getAvailableTools()
        ])
        setRentals(rentalsData.slice(0, 3))
        setTools(toolsData.slice(0, 6))
      } catch (error) {
        console.error('Błąd ładowania danych:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [])

  if (loading) {
    return <Loading />
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Witaj, {user?.firstName}!</h1>
        <p>Panel użytkownika</p>
      </div>

      <div className="dashboard-section">
        <div className="section-header">
          <h2>Moje wypożyczenia</h2>
          <Link to="/my-rentals" className="section-link">
            Zobacz wszystkie
          </Link>
        </div>
        {rentals.length > 0 ? (
          <div className="rentals-list">
            {rentals.map((rental) => (
              <Card key={rental.id} className="rental-item">
                <div className="rental-info">
                  <h3>{rental.toolName}</h3>
                  <p>
                    {new Date(rental.startDate).toLocaleDateString('pl-PL')} -{' '}
                    {new Date(rental.endDate).toLocaleDateString('pl-PL')}
                  </p>
                  <span className={`rental-status rental-status-${rental.status.toLowerCase()}`}>
                    {rental.status === 'PENDING' && 'Oczekujące'}
                    {rental.status === 'ACTIVE' && 'Aktywne'}
                    {rental.status === 'COMPLETED' && 'Zakończone'}
                    {rental.status === 'CANCELLED' && 'Anulowane'}
                    {rental.status === 'OVERDUE' && 'Przeterminowane'}
                  </span>
                </div>
                <div className="rental-price">{rental.totalPrice} zł</div>
              </Card>
            ))}
          </div>
        ) : (
          <p className="empty-message">Brak wypożyczeń</p>
        )}
      </div>

      <div className="dashboard-section">
        <div className="section-header">
          <h2>Dostępne narzędzia</h2>
          <Link to="/tools" className="section-link">
            Zobacz wszystkie
          </Link>
        </div>
        <div className="tools-grid">
          {tools.map((tool) => (
            <Link key={tool.id} to={`/tools/${tool.id}`} className="tool-link">
              <Card className="tool-item">
                <h3>{tool.name}</h3>
                <p className="tool-category">{tool.category}</p>
                <p className="tool-price">{tool.dailyPrice} zł/dzień</p>
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}

export default Dashboard



