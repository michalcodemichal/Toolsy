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
        const data = await getTools()
        setTools(data)
        setFilteredTools(data)
      } catch (error) {
        console.error('Błąd ładowania narzędzi:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchTools()
  }, [])

  useEffect(() => {
    const filterTools = async () => {
      if (searchTerm) {
        try {
          const data = await searchTools(searchTerm)
          setFilteredTools(data)
        } catch (error) {
          console.error('Błąd wyszukiwania:', error)
        }
      } else if (selectedCategory) {
        try {
          const data = await getToolsByCategory(selectedCategory)
          setFilteredTools(data)
        } catch (error) {
          console.error('Błąd filtrowania:', error)
        }
      } else if (sortBy) {
        try {
          const data = await getToolsSorted(sortBy, sortOrder)
          setFilteredTools(data)
        } catch (error) {
          console.error('Błąd sortowania:', error)
        }
      } else {
        setFilteredTools(tools)
      }
    }

    filterTools()
  }, [searchTerm, selectedCategory, sortBy, sortOrder, tools])

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
            setSortBy('')
          }}
          className="search-input"
        />
        <select
          value={selectedCategory}
          onChange={(e) => {
            setSelectedCategory(e.target.value)
            setSearchTerm('')
            setSortBy('')
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
            setSearchTerm('')
            setSelectedCategory('')
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

