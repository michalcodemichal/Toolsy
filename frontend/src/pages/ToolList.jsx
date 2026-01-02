import React, { useEffect, useState } from 'react'
import { getTools, searchTools, getToolsByCategory, getToolsSorted } from '../services/toolService'
import ToolCard from '../components/ToolCard'
import Input from '../components/Input'
import Loading from '../components/Loading'

const ToolList = () => {
  const [tools, setTools] = useState([])
  const [filteredTools, setFilteredTools] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedCategory, setSelectedCategory] = useState('')
  const [sortBy, setSortBy] = useState('')
  const [sortOrder, setSortOrder] = useState('asc')

  useEffect(() => {
    const fetchTools = async () => {
      try {
        console.log('Pobieranie narzędzi...')
        const data = await getTools()
        console.log('Otrzymane dane:', data)
        if (data && Array.isArray(data)) {
          console.log('Liczba narzędzi:', data.length)
          setTools(data)
          setFilteredTools(data)
        } else {
          console.warn('Dane nie są tablicą:', data)
          setTools([])
          setFilteredTools([])
        }
      } catch (error) {
        console.error('Błąd ładowania narzędzi:', error)
        console.error('Szczegóły błędu:', error.response?.data || error.message)
        setTools([])
        setFilteredTools([])
      } finally {
        setLoading(false)
      }
    }

    fetchTools()
  }, [])

  useEffect(() => {
    const filterTools = async () => {
      let filtered = []
      
      if (searchTerm) {
        try {
          filtered = await searchTools(searchTerm)
        } catch (error) {
          console.error('Błąd wyszukiwania:', error)
          filtered = []
        }
      } else if (selectedCategory) {
        try {
          filtered = await getToolsByCategory(selectedCategory)
        } catch (error) {
          console.error('Błąd filtrowania:', error)
          filtered = []
        }
      } else {
        filtered = [...tools]
      }
      
      if (sortBy && filtered.length > 0) {
        filtered.sort((a, b) => {
          let result = 0
          switch (sortBy.toLowerCase()) {
            case 'name':
              result = a.name.localeCompare(b.name)
              break
            case 'price':
              result = parseFloat(a.dailyPrice) - parseFloat(b.dailyPrice)
              break
            case 'category':
              result = a.category.localeCompare(b.category)
              break
            default:
              return 0
          }
          return sortOrder === 'desc' ? -result : result
        })
      }
      
      setFilteredTools(filtered)
    }

    if (tools.length > 0 || searchTerm || selectedCategory) {
      filterTools()
    } else if (tools.length === 0 && !loading) {
      setFilteredTools([])
    }
  }, [searchTerm, selectedCategory, sortBy, sortOrder, tools, loading])

  const categories = [...new Set(tools.map(tool => tool.category))]

  if (loading) {
    return <Loading />
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-gray-900 mb-6">Katalog narzędzi</h1>
        <div className="flex flex-wrap gap-4 mb-6 items-center">
          <div className="w-64">
            <Input
              placeholder="Szukaj narzędzi..."
              value={searchTerm}
              onChange={(e) => {
                setSearchTerm(e.target.value)
                setSelectedCategory('')
              }}
              className="mb-0"
            />
          </div>
          <select
            value={selectedCategory}
            onChange={(e) => {
              setSelectedCategory(e.target.value)
              setSearchTerm('')
            }}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white h-10"
          >
            <option value="">Wszystkie kategorie</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
          <select
            value={sortBy}
            onChange={(e) => {
              setSortBy(e.target.value)
            }}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white h-10"
          >
            <option value="">Sortuj</option>
            <option value="name">Nazwa</option>
            <option value="price">Cena</option>
            <option value="category">Kategoria</option>
          </select>
          {sortBy && (
            <select
              value={sortOrder}
              onChange={(e) => setSortOrder(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white h-10"
            >
              <option value="asc">Rosnąco</option>
              <option value="desc">Malejąco</option>
            </select>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredTools.length > 0 ? (
          filteredTools.map((tool) => <ToolCard key={tool.id} tool={tool} />)
        ) : (
          <div className="col-span-full text-center text-gray-500 py-12 text-lg">
            Brak narzędzi do wyświetlenia
          </div>
        )}
      </div>
    </div>
  )
}

export default ToolList

