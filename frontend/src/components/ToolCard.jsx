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
    <Link to={`/tools/${tool.id}`} className="block h-full group">
      <Card className="h-full flex flex-col p-0 overflow-hidden border-2 border-transparent hover:border-blue-300 transition-all duration-300">
        <div className="w-full h-[250px] overflow-hidden bg-gradient-to-br from-gray-50 to-gray-100 flex items-center justify-center flex-shrink-0 relative">
          {tool.imageUrl ? (
            <img 
              src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
              alt={tool.name}
              className="w-full h-full object-contain transition-transform duration-300 group-hover:scale-110"
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div class="w-full h-full flex items-center justify-center text-gray-400 text-6xl">🔧</div>'
              }}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400 text-6xl">🔧</div>
          )}
          <div className="absolute top-3 right-3">
            <span className={`px-3 py-1.5 rounded-full text-xs font-bold shadow-lg backdrop-blur-sm ${statusColors[tool.status] || statusColors.UNAVAILABLE}`}>
              {statusText[tool.status] || 'Niedostępne'}
            </span>
          </div>
        </div>
        <div className="p-6 flex flex-col flex-1 min-h-[200px] bg-white">
          <h3 className="text-xl font-bold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors">{tool.name}</h3>
          <p className="text-sm text-indigo-600 font-semibold mb-3 bg-indigo-50 px-3 py-1 rounded-full inline-block w-fit">{tool.category}</p>
          <p className="text-gray-600 mb-4 flex-1 line-clamp-3 leading-relaxed">{tool.description}</p>
          <div className="flex justify-between items-center mt-auto pt-4 border-t border-gray-100">
            <div>
              <span className="text-3xl font-extrabold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">{tool.dailyPrice}</span>
              <span className="text-sm text-gray-500 ml-1">zł/dzień</span>
            </div>
            <div className="w-10 h-10 rounded-full bg-gradient-to-r from-blue-500 to-indigo-500 flex items-center justify-center text-white font-bold group-hover:scale-110 transition-transform">
              →
            </div>
          </div>
        </div>
      </Card>
    </Link>
  )
}

export default ToolCard



