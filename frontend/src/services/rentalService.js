import api from './api'

export const createRental = async (rentalData) => {
  const response = await api.post('/rentals', rentalData)
  return response.data
}

export const getMyRentals = async () => {
  const response = await api.get('/rentals/my')
  return response.data
}

export const getAllRentals = async () => {
  const response = await api.get('/rentals/all')
  return response.data
}

export const getRentalById = async (id) => {
  const response = await api.get(`/rentals/${id}`)
  return response.data
}

export const approveRental = async (id) => {
  const response = await api.put(`/rentals/${id}/approve`)
  return response.data
}

export const completeRental = async (id) => {
  const response = await api.put(`/rentals/${id}/complete`)
  return response.data
}

export const cancelRental = async (id) => {
  const response = await api.put(`/rentals/${id}/cancel`)
  return response.data
}



