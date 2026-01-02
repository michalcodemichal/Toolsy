import React from 'react'
import { Link } from 'react-router-dom'
import Card from './Card'

const ToolCard = ({ tool }) => {
  const statusColors = {
    AVAILABLE: 'bg-green-100 text-green-800',
    RENTED: 'bg-red-100 text-red-800',
    MAINTENANCE: 'bg-yellow-100 text-yellow-800',
    UNAVAILABLE: 'bg-gray-100 text-gray-800',
  }

  const statusText = {
    AVAILABLE: 'Dostępne',
    RENTED: 'Wypożyczone',
    MAINTENANCE: 'Konserwacja',
    UNAVAILABLE: 'Niedostępne',
  }

  return (
    <Link to={`/tools/${tool.id}`} className="block h-full">
      <Card className="h-full flex flex-col p-0 hover:shadow-xl transition-shadow">
        <div className="w-full h-[250px] overflow-hidden bg-gray-100 flex items-center justify-center flex-shrink-0">
          {tool.imageUrl ? (
            <img 
              src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
              alt={tool.name}
              className="w-full h-full object-contain"
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div class="w-full h-full flex items-center justify-center text-gray-400">Brak zdjęcia</div>'
              }}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400">Brak zdjęcia</div>
          )}
        </div>
        <div className="p-6 flex flex-col flex-1 min-h-[200px]">
          <h3 className="text-xl font-semibold text-gray-900 mb-2">{tool.name}</h3>
          <p className="text-sm text-gray-600 mb-3">{tool.category}</p>
          <p className="text-gray-700 mb-4 flex-1 line-clamp-3">{tool.description}</p>
          <div className="flex justify-between items-center mt-auto">
            <span className="text-2xl font-bold text-green-600">{tool.dailyPrice} zł/dzień</span>
            <span className={`px-3 py-1 rounded-full text-xs font-medium ${statusColors[tool.status] || statusColors.UNAVAILABLE}`}>
              {statusText[tool.status] || 'Niedostępne'}
            </span>
          </div>
        </div>
      </Card>
    </Link>
  )
}

export default ToolCard



