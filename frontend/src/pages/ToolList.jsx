import React, { useEffect, useState } from 'react'
import { getTools, searchTools, getToolsByCategory } from '../services/toolService'
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
      } else {
        setFilteredTools(tools)
      }
    }

    filterTools()
  }, [searchTerm, selectedCategory, tools])

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

