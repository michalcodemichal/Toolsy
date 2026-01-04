import api from './api'

export const getTools = async () => {
  const response = await api.get('/tools')
  return response.data
}

export const getAvailableTools = async () => {
  const response = await api.get('/tools/available')
  return response.data
}

export const getToolById = async (id, startDate = null, endDate = null) => {
  let url = `/tools/${id}`
  if (startDate && endDate) {
    url += `?startDate=${startDate}&endDate=${endDate}`
  }
  const response = await api.get(url)
  return response.data
}

export const searchTools = async (query) => {
  const response = await api.get(`/tools/search?q=${query}`)
  return response.data
}

export const getToolsByCategory = async (category) => {
  const response = await api.get(`/tools/category/${category}`)
  return response.data
}

export const createTool = async (toolData) => {
  const response = await api.post('/tools', toolData)
  return response.data
}

export const updateTool = async (id, toolData) => {
  const response = await api.put(`/tools/${id}`, toolData)
  return response.data
}

export const deleteTool = async (id) => {
  await api.delete(`/tools/${id}`)
}

export const getToolsSorted = async (sortBy, sortOrder = 'asc') => {
  const response = await api.get(`/tools/sorted?sortBy=${sortBy}&sortOrder=${sortOrder}`)
  return response.data
}

export const getAvailableToolsForPeriod = async (startDate, endDate) => {
  const response = await api.get(`/tools/available-for-period?startDate=${startDate}&endDate=${endDate}`)
  return response.data
}

