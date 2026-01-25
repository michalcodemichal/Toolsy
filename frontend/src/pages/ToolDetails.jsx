import React, { useEffect, useMemo, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import { getToolById } from '../services/toolService'
import { createToolReview, getToolReviews } from '../services/reviewService'
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
  const [reviews, setReviews] = useState([])
  const [reviewsLoading, setReviewsLoading] = useState(true)
  const [submittingReview, setSubmittingReview] = useState(false)
  const [reviewData, setReviewData] = useState({
    rating: 5,
    comment: ''
  })
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
      setReviewsLoading(true)
      try {
        const data = await getToolReviews(id)
        setReviews(Array.isArray(data) ? data : [])
      } catch (error) {
        setReviews([])
      } finally {
        setReviewsLoading(false)
      }
    }

    if (id) {
      fetchReviews()
    }
  }, [id])

  const hasUserReviewed = useMemo(() => {
    if (!user) {
      return false
    }
    return reviews.some((review) => review.userId === user.id)
  }, [reviews, user])

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

  const handleReviewSubmit = async (e) => {
    e.preventDefault()
    if (!user) {
      toast.error('Musisz być zalogowany, aby dodać recenzję')
      navigate('/login')
      return
    }

    setSubmittingReview(true)
    try {
      await createToolReview(id, {
        rating: parseInt(reviewData.rating, 10),
        comment: reviewData.comment
      })
      toast.success('Recenzja dodana')
      setReviewData({ rating: 5, comment: '' })
      const [updatedTool, updatedReviews] = await Promise.all([
        getToolById(id),
        getToolReviews(id)
      ])
      setTool(updatedTool)
      setReviews(Array.isArray(updatedReviews) ? updatedReviews : [])
    } catch (error) {
      const message = error.response?.data?.message || 'Nie udało się dodać recenzji'
      toast.error(message)
    } finally {
      setSubmittingReview(false)
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
        <div className="w-full min-h-[400px] max-h-[600px] bg-gradient-to-br from-gray-50 to-gray-100 dark:from-slate-800 dark:to-slate-900 flex items-center justify-center p-8 rounded-2xl">
          {tool.imageUrl ? (
            <img 
              src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
              alt={tool.name}
              className="max-w-full max-h-[600px] w-auto h-auto object-contain"
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div class="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-8xl">🔧</div>'
              }}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-8xl">🔧</div>
          )}
        </div>
        <div className="space-y-6">
          <Card variant="gradient">
            <div className="mb-4">
              <h1 className="text-4xl font-extrabold text-gray-900 dark:text-gray-100 mb-3">{tool.name}</h1>
              <span className="inline-block px-4 py-2 bg-indigo-100 dark:bg-indigo-900/30 text-indigo-700 dark:text-indigo-300 rounded-full text-sm font-bold">
                {tool.category}
              </span>
            </div>
            <p className="text-gray-700 dark:text-gray-300 text-lg leading-relaxed mb-6">{tool.description}</p>
            <div className="flex items-center gap-3 mb-6">
              <div className="flex items-center gap-1 text-yellow-500 text-lg">
                {'★'.repeat(Math.round(tool.averageRating || 0))}
                <span className="text-gray-400 dark:text-gray-500 text-base">
                  {'★'.repeat(Math.max(0, 5 - Math.round(tool.averageRating || 0)))}
                </span>
              </div>
              <span className="text-sm text-gray-600 dark:text-gray-400">
                {Number(tool.averageRating || 0).toFixed(1)} / 5 ({tool.reviewCount || 0} recenzji)
              </span>
            </div>
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
            <div className="flex items-center gap-3">
              <span className={`px-4 py-2 rounded-full text-sm font-bold shadow-md ${
                (tool.quantity === 0 || tool.status === 'UNAVAILABLE') ? 'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-300' :
                tool.status === 'AVAILABLE' ? 'bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-300' :
                tool.status === 'RENTED' ? 'bg-red-100 dark:bg-red-900/30 text-red-800 dark:text-red-300' :
                tool.status === 'MAINTENANCE' ? 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-800 dark:text-yellow-300' :
                'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-300'
              }`}>
                {(tool.quantity === 0 || tool.status === 'UNAVAILABLE') && '🚫 Niedostępne'}
                {tool.quantity > 0 && tool.status === 'AVAILABLE' && '✅ Dostępne'}
                {tool.quantity > 0 && tool.status === 'RENTED' && '❌ Wypożyczone'}
                {tool.quantity > 0 && tool.status === 'MAINTENANCE' && '🔧 Konserwacja'}
              </span>
            </div>
          </Card>
          {user && tool.status === 'AVAILABLE' && tool.quantity > 0 && (
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
          <Card>
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                Recenzje
              </h2>
              <span className="text-sm text-gray-500 dark:text-gray-400">
                {tool.reviewCount || reviews.length} opinii
              </span>
            </div>
            {reviewsLoading ? (
              <div className="text-center py-8">
                <div className="inline-block animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
              </div>
            ) : reviews.length === 0 ? (
              <p className="text-gray-500 dark:text-gray-400">Brak recenzji dla tego narzędzia.</p>
            ) : (
              <div className="space-y-4">
                {reviews.map((review) => (
                  <div key={review.id} className="border border-gray-200 dark:border-gray-700 rounded-xl p-4">
                    <div className="flex items-center justify-between mb-2">
                      <div className="text-sm font-semibold text-gray-900 dark:text-gray-100">
                        {review.firstName || review.lastName
                          ? `${review.firstName || ''} ${review.lastName || ''}`.trim()
                          : review.username}
                      </div>
                      <div className="text-yellow-500 text-sm">
                        {'★'.repeat(review.rating)}
                        <span className="text-gray-300 dark:text-gray-600">
                          {'★'.repeat(Math.max(0, 5 - review.rating))}
                        </span>
                      </div>
                    </div>
                    {review.comment && (
                      <p className="text-gray-600 dark:text-gray-300 text-sm">{review.comment}</p>
                    )}
                    <div className="text-xs text-gray-400 dark:text-gray-500 mt-2">
                      {new Date(review.createdAt).toLocaleDateString('pl-PL')}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </Card>
          <Card>
            <h3 className="text-xl font-bold text-gray-900 dark:text-gray-100 mb-4">Dodaj recenzję</h3>
            {!user ? (
              <div className="text-gray-600 dark:text-gray-300">
                Zaloguj się, aby dodać recenzję.
              </div>
            ) : hasUserReviewed ? (
              <div className="text-gray-600 dark:text-gray-300">
                Dodałeś już recenzję do tego narzędzia.
              </div>
            ) : (
              <form onSubmit={handleReviewSubmit} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Ocena
                  </label>
                  <select
                    value={reviewData.rating}
                    onChange={(e) => setReviewData({ ...reviewData, rating: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 shadow-sm text-gray-900 dark:text-gray-100"
                  >
                    {[5, 4, 3, 2, 1].map((rating) => (
                      <option key={rating} value={rating}>
                        {rating} - {rating === 5 ? 'Świetna' : rating === 4 ? 'Dobra' : rating === 3 ? 'OK' : rating === 2 ? 'Słaba' : 'Zła'}
                      </option>
                    ))}
                  </select>
                </div>
                <Input
                  label="Komentarz (opcjonalnie)"
                  type="textarea"
                  value={reviewData.comment}
                  onChange={(e) => setReviewData({ ...reviewData, comment: e.target.value })}
                  placeholder="Napisz krótką opinię o narzędziu"
                />
                <Button type="submit" disabled={submittingReview}>
                  {submittingReview ? 'Dodawanie...' : 'Dodaj recenzję'}
                </Button>
              </form>
            )}
          </Card>
        </div>
      </div>
    </div>
  )
}

export default ToolDetails



