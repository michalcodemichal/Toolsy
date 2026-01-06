import React from 'react'
import Card from './Card'

const ReviewList = ({ reviews }) => {
  if (!reviews || reviews.length === 0) {
    return (
      <Card className="text-center py-8">
        <p className="text-gray-500 dark:text-gray-400">
          Brak recenzji. Bądź pierwszym, który oceni to narzędzie!
        </p>
      </Card>
    )
  }

  const renderStars = (rating) => {
    return '⭐'.repeat(rating) + '☆'.repeat(5 - rating)
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('pl-PL', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  return (
    <div className="space-y-4">
      {reviews.map((review) => (
        <Card key={review.id} className="p-6">
          <div className="flex items-start justify-between mb-3">
            <div>
              <h4 className="font-semibold text-gray-900 dark:text-gray-100">
                {review.username}
              </h4>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                {formatDate(review.createdAt)}
              </p>
            </div>
            <div className="text-lg">
              {renderStars(review.rating)}
            </div>
          </div>
          <p className="text-gray-700 dark:text-gray-300 whitespace-pre-wrap">
            {review.comment}
          </p>
        </Card>
      ))}
    </div>
  )
}

export default ReviewList

