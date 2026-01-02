import api from './api'

export const getCurrentUser = async () => {
  const response = await api.get('/users/me')
  return response.data
}

export const getAllUsers = async () => {
  const response = await api.get('/users')
  return response.data
}

export const getUserById = async (id) => {
  const response = await api.get(`/users/${id}`)
  return response.data
}

export const deactivateUser = async (id) => {
  await api.put(`/users/${id}/deactivate`)
}

export const activateUser = async (id) => {
  await api.put(`/users/${id}/activate`)
}



