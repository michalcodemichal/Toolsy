import React, { useState } from 'react'
import { toast } from 'react-toastify'
import { useDarkMode } from '../context/DarkModeContext'
import Button from './Button'
import { createReview, updateReview, deleteReview } from '../services/reviewService'

const ReviewForm = ({ toolId, existingReview, onReviewSubmitted, onReviewDeleted }) => {
  const { isDark } = useDarkMode()
  const [rating, setRating] = useState(existingReview?.rating || 0)
  const [hoveredRating, setHoveredRating] = useState(0)
  const [comment, setComment] = useState(existingReview?.comment || '')
  const [submitting, setSubmitting] = useState(false)
  const [deleting, setDeleting] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (rating === 0) {
      toast.error('Wybierz ocenę')
      return
    }

    setSubmitting(true)
    try {
      if (existingReview) {
        await updateReview(existingReview.id, {
          toolId,
          rating,
          comment
        })
        toast.success('Recenzja zaktualizowana')
      } else {
        await createReview({
          toolId,
          rating,
          comment
        })
        toast.success('Recenzja dodana')
      }
      setComment('')
      setRating(0)
      if (onReviewSubmitted) onReviewSubmitted()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd podczas zapisywania recenzji')
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!existingReview) return
    if (!window.confirm('Czy na pewno chcesz usunąć tę recenzję?')) return

    setDeleting(true)
    try {
      await deleteReview(existingReview.id)
      toast.success('Recenzja usunięta')
      setComment('')
      setRating(0)
      if (onReviewDeleted) onReviewDeleted()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Błąd podczas usuwania recenzji')
    } finally {
      setDeleting(false)
    }
  }

  const renderStars = () => {
    return Array.from({ length: 5 }, (_, i) => {
      const starValue = i + 1
      const isActive = starValue <= (hoveredRating || rating)
      return (
        <button
          key={i}
          type="button"
          onClick={() => setRating(starValue)}
          onMouseEnter={() => setHoveredRating(starValue)}
          onMouseLeave={() => setHoveredRating(0)}
          className={`text-2xl transition-all ${
            isActive ? 'text-yellow-400 scale-110' : 'text-gray-300 dark:text-gray-600'
          } hover:scale-125`}
        >
          ★
        </button>
      )
    })
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className={`block mb-2 font-semibold ${isDark ? 'text-gray-200' : 'text-gray-700'}`}>
          Twoja ocena
        </label>
        <div className="flex items-center gap-2">{renderStars()}</div>
      </div>

      <div>
        <label className={`block mb-2 font-semibold ${isDark ? 'text-gray-200' : 'text-gray-700'}`}>
          Komentarz (opcjonalnie)
        </label>
        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          rows={4}
          maxLength={1000}
          className={`w-full px-4 py-2 rounded-lg border ${
            isDark
              ? 'bg-slate-700 border-slate-600 text-gray-200'
              : 'bg-white border-gray-300 text-gray-800'
          } focus:outline-none focus:ring-2 focus:ring-blue-500`}
          placeholder="Napisz swoją opinię..."
        />
        <div className={`text-sm mt-1 ${isDark ? 'text-gray-400' : 'text-gray-500'}`}>
          {comment.length}/1000 znaków
        </div>
      </div>

      <div className="flex gap-2">
        <Button type="submit" disabled={submitting || rating === 0}>
          {submitting ? 'Zapisywanie...' : existingReview ? 'Zaktualizuj recenzję' : 'Dodaj recenzję'}
        </Button>
        {existingReview && (
          <Button
            type="button"
            variant="danger"
            onClick={handleDelete}
            disabled={deleting}
          >
            {deleting ? 'Usuwanie...' : 'Usuń'}
          </Button>
        )}
      </div>
    </form>
  )
}

export default ReviewForm

