import api from './api'

export const getReviewsByTool = async (toolId) => {
  const response = await api.get(`/reviews/tool/${toolId}`)
  return response.data
}

export const getMyReviewForTool = async (toolId) => {
  const response = await api.get(`/reviews/tool/${toolId}/my`)
  return response.data
}

export const createReview = async (reviewData) => {
  const response = await api.post('/reviews', reviewData)
  return response.data
}

export const updateReview = async (reviewId, reviewData) => {
  const response = await api.put(`/reviews/${reviewId}`, reviewData)
  return response.data
}

export const deleteReview = async (reviewId) => {
  await api.delete(`/reviews/${reviewId}`)
}

