import React from 'react'
import { useDarkMode } from '../context/DarkModeContext'

const ReviewList = ({ reviews }) => {
  const { isDark } = useDarkMode()

  if (!reviews || reviews.length === 0) {
    return (
      <div className={`text-center py-8 ${isDark ? 'text-gray-400' : 'text-gray-600'}`}>
        Brak recenzji. Bądź pierwszy!
      </div>
    )
  }

  const renderStars = (rating) => {
    return Array.from({ length: 5 }, (_, i) => (
      <span
        key={i}
        className={i < rating ? 'text-yellow-400' : 'text-gray-300 dark:text-gray-600'}
      >
        ★
      </span>
    ))
  }

  return (
    <div className="space-y-4">
      {reviews.map((review) => (
        <div
          key={review.id}
          className={`p-4 rounded-lg border ${
            isDark
              ? 'bg-slate-800 border-slate-700'
              : 'bg-white border-gray-200'
          }`}
        >
          <div className="flex items-start justify-between mb-2">
            <div>
              <div className="font-semibold text-gray-800 dark:text-gray-200">
                {review.username}
              </div>
              <div className="flex items-center gap-1 mt-1">
                {renderStars(review.rating)}
                <span className="ml-2 text-sm text-gray-600 dark:text-gray-400">
                  {review.rating}/5
                </span>
              </div>
            </div>
            <div className="text-sm text-gray-500 dark:text-gray-400">
              {new Date(review.createdAt).toLocaleDateString('pl-PL')}
            </div>
          </div>
          {review.comment && (
            <p className="mt-2 text-gray-700 dark:text-gray-300">{review.comment}</p>
          )}
        </div>
      ))}
    </div>
  )
}

export default ReviewList

