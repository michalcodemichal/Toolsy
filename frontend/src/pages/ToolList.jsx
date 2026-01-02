import React, { useEffect, useState } from 'react'
import { getTools, searchTools, getToolsByCategory, getToolsSorted } from '../services/toolService'
import ToolCard from '../components/ToolCard'
import Input from '../components/Input'
import Loading from '../components/Loading'
import './ToolList.css'

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
    <div className="tool-list-container">
      <div className="tool-list-header">
        <h1>Katalog narzędzi</h1>
      </div>

      <div className="tool-list-filters">
        <Input
          placeholder="Szukaj narzędzi..."
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value)
            setSelectedCategory('')
          }}
          className="search-input"
        />
        <select
          value={selectedCategory}
          onChange={(e) => {
            setSelectedCategory(e.target.value)
            setSearchTerm('')
          }}
          className="category-select"
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
          className="sort-select"
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
            className="sort-order-select"
          >
            <option value="asc">Rosnąco</option>
            <option value="desc">Malejąco</option>
          </select>
        )}
      </div>

      <div className="tool-list-grid">
        {filteredTools.length > 0 ? (
          filteredTools.map((tool) => <ToolCard key={tool.id} tool={tool} />)
        ) : (
          <p className="empty-message">Brak narzędzi do wyświetlenia</p>
        )}
      </div>
    </div>
  )
}

export default ToolList

