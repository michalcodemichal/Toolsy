import api from './api'

export const getToolReviews = async (toolId) => {
  const response = await api.get(`/tools/${toolId}/reviews`)
  return response.data
}

export const createToolReview = async (toolId, reviewData) => {
  const response = await api.post(`/tools/${toolId}/reviews`, reviewData)
  return response.data
}
