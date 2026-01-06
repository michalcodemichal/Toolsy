import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import { getToolById } from '../services/toolService'
import { createRental } from '../services/rentalService'
import { getReviewsByToolId } from '../services/reviewService'
import Card from '../components/Card'
import Button from '../components/Button'
import Input from '../components/Input'
import Loading from '../components/Loading'
import ReviewList from '../components/ReviewList'
import ReviewForm from '../components/ReviewForm'
import './ToolDetails.css'

const ToolDetails = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [tool, setTool] = useState(null)
  const [loading, setLoading] = useState(true)
  const [renting, setRenting] = useState(false)
  const [showRentalForm, setShowRentalForm] = useState(false)
  const [reviews, setReviews] = useState([])
  const [loadingReviews, setLoadingReviews] = useState(true)
  const [userReview, setUserReview] = useState(null)
  const [rentalData, setRentalData] = useState({
    startDate: '',
    endDate: '',
    quantity: 1,
    notes: ''
  })

  useEffect(() => {
    const fetchTool = async () => {
      try {
        const startDate = rentalData.startDate || null
        const endDate = rentalData.endDate || null
        const data = await getToolById(id, startDate, endDate)
        setTool(data)
      } catch (error) {
        toast.error('Błąd ładowania narzędzia')
        navigate('/tools')
      } finally {
        setLoading(false)
      }
    }

    fetchTool()
  }, [id, navigate, rentalData.startDate, rentalData.endDate])

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        setLoadingReviews(true)
        const reviewsData = await getReviewsByToolId(id)
        setReviews(reviewsData)
        
        // Find user's review if logged in
        if (user) {
          const review = reviewsData.find(r => r.userId === user.id)
          setUserReview(review || null)
        } else {
          setUserReview(null)
        }
      } catch (error) {
        console.error('Błąd ładowania recenzji:', error)
      } finally {
        setLoadingReviews(false)
      }
    }

    if (id) {
      fetchReviews()
    }
  }, [id, user])

  const handleReviewSubmitted = async () => {
    // Reload reviews and tool
    try {
      const reviewsData = await getReviewsByToolId(id)
      setReviews(reviewsData)
      
      if (user) {
        const review = reviewsData.find(r => r.userId === user.id)
        setUserReview(review || null)
      }

      // Reload tool to get updated rating
      const toolData = await getToolById(id)
      setTool(toolData)
    } catch (error) {
      console.error('Błąd odświeżania recenzji:', error)
    }
  }

  const handleReviewDeleted = async () => {
    setUserReview(null)
    await handleReviewSubmitted()
  }

  const handleRentalSubmit = async (e) => {
    e.preventDefault()
    if (!user) {
      toast.error('Musisz być zalogowany, aby wypożyczyć narzędzie')
      navigate('/login')
      return
    }

    if (!rentalData.startDate || !rentalData.endDate) {
      toast.error('Proszę wybrać datę rozpoczęcia i zakończenia')
      return
    }

    setRenting(true)
    try {
      console.log('Tworzenie wypożyczenia:', {
        toolId: parseInt(id),
        startDate: rentalData.startDate,
        endDate: rentalData.endDate,
        notes: rentalData.notes
      })
      await createRental({
        toolId: parseInt(id),
        startDate: rentalData.startDate,
        endDate: rentalData.endDate,
        quantity: parseInt(rentalData.quantity) || 1,
        notes: rentalData.notes || ''
      })
      toast.success('Wypożyczenie utworzone pomyślnie')
      setShowRentalForm(false)
      setRentalData({ startDate: '', endDate: '', quantity: 1, notes: '' })
      const updatedTool = await getToolById(id)
      setTool(updatedTool)
    } catch (error) {
      console.error('Błąd tworzenia wypożyczenia:', error)
      console.error('Szczegóły błędu:', error.response?.data || error.message)
      const errorMessage = error.response?.data?.message || 
                          error.response?.data?.error || 
                          error.message || 
                          'Błąd tworzenia wypożyczenia'
      toast.error(errorMessage)
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
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <Card className="p-0 overflow-hidden">
          <div className="w-full h-[500px] bg-gradient-to-br from-gray-50 to-gray-100 dark:from-slate-800 dark:to-slate-900 flex items-center justify-center">
            {tool.imageUrl ? (
              <img 
                src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
                alt={tool.name}
                className="w-full h-full object-contain"
                onError={(e) => {
                  e.target.style.display = 'none'
                  e.target.parentElement.innerHTML = '<div class="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-8xl">🔧</div>'
                }}
              />
            ) : (
              <div className="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-8xl">🔧</div>
            )}
          </div>
        </Card>
        <div className="space-y-6">
          <Card variant="gradient">
            <div className="mb-4">
              <h1 className="text-4xl font-extrabold text-gray-900 dark:text-gray-100 mb-3">{tool.name}</h1>
              <span className="inline-block px-4 py-2 bg-indigo-100 dark:bg-indigo-900/30 text-indigo-700 dark:text-indigo-300 rounded-full text-sm font-bold">
                {tool.category}
              </span>
            </div>
            <p className="text-gray-700 dark:text-gray-300 text-lg leading-relaxed mb-6">{tool.description}</p>
            <div className="grid grid-cols-2 gap-4 mb-6">
              <div className="bg-gradient-to-br from-green-50 to-emerald-50 dark:from-green-900/20 dark:to-emerald-900/20 p-4 rounded-xl border-2 border-green-200 dark:border-green-800">
                <p className="text-sm text-gray-600 dark:text-gray-300 mb-1">Cena dzienna</p>
                <p className="text-3xl font-extrabold bg-gradient-to-r from-green-600 to-emerald-600 dark:from-green-400 dark:to-emerald-400 bg-clip-text text-transparent">
                  {tool.dailyPrice} zł
                </p>
              </div>
              <div className="bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-blue-900/20 dark:to-indigo-900/20 p-4 rounded-xl border-2 border-blue-200 dark:border-blue-800">
                <p className="text-sm text-gray-600 dark:text-gray-300 mb-1">Dostępna ilość</p>
                <p className="text-3xl font-extrabold bg-gradient-to-r from-blue-600 to-indigo-600 dark:from-blue-400 dark:to-indigo-400 bg-clip-text text-transparent">
                  {tool.quantity}
                </p>
              </div>
            </div>
            {(tool.averageRating || tool.reviewCount > 0) && (
              <div className="mb-6 p-4 bg-gradient-to-br from-yellow-50 to-amber-50 dark:from-yellow-900/20 dark:to-amber-900/20 rounded-xl border-2 border-yellow-200 dark:border-yellow-800">
                <div className="flex items-center gap-4">
                  <div>
                    <p className="text-sm text-gray-600 dark:text-gray-300 mb-1">Średnia ocena</p>
                    <div className="flex items-center gap-2">
                      <span className="text-2xl font-bold text-yellow-600 dark:text-yellow-400">
                        {tool.averageRating ? tool.averageRating.toFixed(1) : '0.0'}
                      </span>
                      <span className="text-xl">⭐</span>
                    </div>
                  </div>
                  <div className="h-12 w-px bg-yellow-300 dark:bg-yellow-700"></div>
                  <div>
                    <p className="text-sm text-gray-600 dark:text-gray-300 mb-1">Liczba recenzji</p>
                    <p className="text-2xl font-bold text-yellow-600 dark:text-yellow-400">
                      {tool.reviewCount || 0}
                    </p>
                  </div>
                </div>
              </div>
            )}
            <div className="flex items-center gap-3">
              <span className={`px-4 py-2 rounded-full text-sm font-bold shadow-md ${
                tool.status === 'AVAILABLE' ? 'bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-300' :
                tool.status === 'RENTED' ? 'bg-red-100 dark:bg-red-900/30 text-red-800 dark:text-red-300' :
                tool.status === 'MAINTENANCE' ? 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-800 dark:text-yellow-300' :
                'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-300'
              }`}>
                {tool.status === 'AVAILABLE' && '✅ Dostępne'}
                {tool.status === 'RENTED' && '❌ Wypożyczone'}
                {tool.status === 'MAINTENANCE' && '🔧 Konserwacja'}
                {tool.status === 'UNAVAILABLE' && '🚫 Niedostępne'}
              </span>
            </div>
          </Card>
          {user && tool.status === 'AVAILABLE' && (
            <Card variant="gradient">
              {!showRentalForm ? (
                <Button onClick={() => setShowRentalForm(true)} className="w-full text-lg py-4">
                  🛒 Wypożycz narzędzie
                </Button>
              ) : (
                <div>
                  <h3 className="text-2xl font-bold text-gray-900 dark:text-gray-100 mb-6 flex items-center gap-2">
                    <span>📝</span> Formularz wypożyczenia
                  </h3>
                  <form onSubmit={handleRentalSubmit} className="space-y-4">
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
                      label="Ilość"
                      type="number"
                      value={rentalData.quantity}
                      onChange={(e) => {
                        const qty = Math.max(1, Math.min(parseInt(e.target.value) || 1, tool.quantity))
                        setRentalData({ ...rentalData, quantity: qty })
                      }}
                      required
                      min={1}
                      max={tool.quantity}
                    />
                    <Input
                      label="Uwagi (opcjonalnie)"
                      type="textarea"
                      value={rentalData.notes}
                      onChange={(e) =>
                        setRentalData({ ...rentalData, notes: e.target.value })
                      }
                    />
                    <div className="flex gap-4 pt-4">
                      <Button type="submit" disabled={renting} className="flex-1">
                        {renting ? '⏳ Tworzenie...' : '✅ Potwierdź wypożyczenie'}
                      </Button>
                      <Button
                        type="button"
                        variant="secondary"
                        onClick={() => {
                          setShowRentalForm(false)
                          setRentalData({ startDate: '', endDate: '', quantity: 1, notes: '' })
                        }}
                      >
                        Anuluj
                      </Button>
                    </div>
                  </form>
                </div>
              )}
            </Card>
          )}
          {!user && (
            <Card className="text-center py-8">
              <p className="text-gray-600 dark:text-gray-300 text-lg mb-4">
                Zaloguj się, aby wypożyczyć narzędzie
              </p>
              <Button onClick={() => navigate('/login')} className="gradient-primary">
                Zaloguj się
              </Button>
            </Card>
          )}
        </div>
      </div>

      {/* Reviews Section */}
      <div className="mt-12">
        <h2 className="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">
          ⭐ Recenzje ({reviews.length})
        </h2>
        
        {user && (
          <div className="mb-8">
            <ReviewForm
              toolId={id}
              existingReview={userReview}
              onReviewSubmitted={handleReviewSubmitted}
              onReviewDeleted={handleReviewDeleted}
            />
          </div>
        )}

        {loadingReviews ? (
          <Card className="text-center py-8">
            <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          </Card>
        ) : (
          <ReviewList reviews={reviews} />
        )}
      </div>
    </div>
  )
}

export default ToolDetails



