import React, { useState } from 'react'
import { toast } from 'react-toastify'
import { useAuth } from '../context/AuthContext'
import { createReview, updateReview, deleteReview } from '../services/reviewService'
import Card from './Card'
import Button from './Button'
import Input from './Input'

const ReviewForm = ({ toolId, existingReview, onReviewSubmitted, onReviewDeleted }) => {
  const { user } = useAuth()
  const [rating, setRating] = useState(existingReview?.rating || 0)
  const [comment, setComment] = useState(existingReview?.comment || '')
  const [submitting, setSubmitting] = useState(false)

  if (!user) {
    return (
      <Card className="text-center py-6">
        <p className="text-gray-600 dark:text-gray-300 mb-4">
          Zaloguj się, aby dodać recenzję
        </p>
      </Card>
    )
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (rating === 0) {
      toast.error('Wybierz ocenę')
      return
    }

    if (!comment.trim()) {
      toast.error('Dodaj komentarz')
      return
    }

    setSubmitting(true)
    try {
      const reviewData = {
        toolId: parseInt(toolId),
        rating,
        comment: comment.trim()
      }

      if (existingReview) {
        await updateReview(existingReview.id, reviewData)
        toast.success('Recenzja zaktualizowana')
      } else {
        await createReview(reviewData)
        toast.success('Recenzja dodana')
      }

      setComment('')
      setRating(0)
      if (onReviewSubmitted) {
        onReviewSubmitted()
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || 
                          error.response?.data?.error || 
                          'Błąd podczas zapisywania recenzji'
      toast.error(errorMessage)
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!existingReview) return
    
    if (!window.confirm('Czy na pewno chcesz usunąć tę recenzję?')) {
      return
    }

    try {
      await deleteReview(existingReview.id)
      toast.success('Recenzja usunięta')
      setComment('')
      setRating(0)
      if (onReviewDeleted) {
        onReviewDeleted()
      }
    } catch (error) {
      toast.error('Błąd podczas usuwania recenzji')
    }
  }

  const renderStarButtons = () => {
    return (
      <div className="flex gap-2 mb-4">
        {[1, 2, 3, 4, 5].map((star) => (
          <button
            key={star}
            type="button"
            onClick={() => setRating(star)}
            className={`text-3xl transition-transform hover:scale-110 ${
              star <= rating ? 'text-yellow-400' : 'text-gray-300 dark:text-gray-600'
            }`}
          >
            ⭐
          </button>
        ))}
      </div>
    )
  }

  return (
    <Card className="p-6">
      <h3 className="text-xl font-bold text-gray-900 dark:text-gray-100 mb-4">
        {existingReview ? '✏️ Edytuj recenzję' : '⭐ Dodaj recenzję'}
      </h3>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Ocena
          </label>
          {renderStarButtons()}
          {rating > 0 && (
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Wybrano: {rating} {rating === 1 ? 'gwiazdka' : rating < 5 ? 'gwiazdki' : 'gwiazdek'}
            </p>
          )}
        </div>

        <div className="mb-4">
          <Input
            label="Komentarz"
            type="textarea"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            required
            rows={4}
            placeholder="Napisz swoją opinię o tym narzędziu..."
          />
        </div>

        <div className="flex gap-3">
          <Button type="submit" disabled={submitting || rating === 0 || !comment.trim()}>
            {submitting ? '⏳ Zapisywanie...' : existingReview ? '💾 Zaktualizuj' : '✅ Opublikuj'}
          </Button>
          {existingReview && (
            <Button type="button" variant="secondary" onClick={handleDelete}>
              🗑️ Usuń
            </Button>
          )}
        </div>
      </form>
    </Card>
  )
}

export default ReviewForm

