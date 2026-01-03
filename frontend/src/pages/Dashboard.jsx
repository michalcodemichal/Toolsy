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
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-10">
        <div className="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 dark:from-blue-700 dark:via-indigo-700 dark:to-purple-700 rounded-3xl p-8 text-white shadow-2xl mb-8">
          <h1 className="text-4xl font-extrabold mb-2">Witaj, {user?.firstName}! 👋</h1>
          <p className="text-xl text-blue-100 dark:text-blue-200">Panel użytkownika - zarządzaj swoimi wypożyczeniami</p>
        </div>
      </div>

      <div className="mb-10">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-3xl font-bold text-gray-800 dark:text-gray-100 flex items-center gap-3">
            <span className="text-4xl">📋</span>
            Moje wypożyczenia
          </h2>
          <Link to="/my-rentals" className="text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 font-semibold flex items-center gap-2 transition-colors hover:gap-3">
            Zobacz wszystkie <span>→</span>
          </Link>
        </div>
        {rentals.length > 0 ? (
          <div className="grid gap-4">
            {rentals.map((rental) => (
              <Card key={rental.id} variant="gradient" className="flex justify-between items-center p-6">
                <div className="flex-1">
                  <h3 className="text-xl font-bold text-gray-900 dark:text-gray-100 mb-2">{rental.toolName}</h3>
                  <p className="text-gray-600 dark:text-gray-300 mb-3 flex items-center gap-2">
                    <span>📅</span>
                    {new Date(rental.startDate).toLocaleDateString('pl-PL')} -{' '}
                    {new Date(rental.endDate).toLocaleDateString('pl-PL')}
                  </p>
                  <span className={`inline-block px-4 py-1.5 rounded-full text-sm font-semibold shadow-md ${
                    rental.status === 'PENDING' ? 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-800 dark:text-yellow-300' :
                    rental.status === 'ACTIVE' ? 'bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-300' :
                    rental.status === 'COMPLETED' ? 'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-300' :
                    rental.status === 'CANCELLED' ? 'bg-red-100 dark:bg-red-900/30 text-red-800 dark:text-red-300' :
                    'bg-orange-100 dark:bg-orange-900/30 text-orange-800 dark:text-orange-300'
                  }`}>
                    {rental.status === 'PENDING' && '⏳ Oczekujące'}
                    {rental.status === 'ACTIVE' && '✅ Aktywne'}
                    {rental.status === 'COMPLETED' && '✓ Zakończone'}
                    {rental.status === 'CANCELLED' && '❌ Anulowane'}
                    {rental.status === 'OVERDUE' && '⚠️ Przeterminowane'}
                  </span>
                </div>
                <div className="text-right ml-6">
                  <div className="text-3xl font-extrabold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
                    {rental.totalPrice} zł
                  </div>
                </div>
              </Card>
            ))}
          </div>
        ) : (
          <Card className="text-center py-12">
            <div className="text-6xl mb-4">📦</div>
            <p className="text-gray-500 dark:text-gray-400 text-lg">Brak wypożyczeń</p>
          </Card>
        )}
      </div>

      <div>
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-3xl font-bold text-gray-800 dark:text-gray-100 flex items-center gap-3">
            <span className="text-4xl">🔧</span>
            Dostępne narzędzia
          </h2>
          <Link to="/tools" className="text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 font-semibold flex items-center gap-2 transition-colors hover:gap-3">
            Zobacz wszystkie <span>→</span>
          </Link>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {tools.map((tool) => (
            <Link key={tool.id} to={`/tools/${tool.id}`} className="block">
              <Card variant="gradient" className="text-center p-6 hover:scale-105 transition-transform">
                <div className="text-5xl mb-4">🔨</div>
                <h3 className="text-lg font-bold text-gray-900 dark:text-gray-100 mb-2">{tool.name}</h3>
                <p className="text-sm text-indigo-600 dark:text-indigo-400 font-semibold mb-3 bg-indigo-50 dark:bg-indigo-900/30 px-3 py-1 rounded-full inline-block">{tool.category}</p>
                <p className="text-2xl font-extrabold bg-gradient-to-r from-green-600 to-emerald-600 dark:from-green-400 dark:to-emerald-400 bg-clip-text text-transparent">
                  {tool.dailyPrice} zł/dzień
                </p>
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}

export default Dashboard



