import React from 'react'
import { Link } from 'react-router-dom'
import Card from './Card'

const ToolCard = ({ tool }) => {
  const statusColors = {
    AVAILABLE: 'bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-300',
    RENTED: 'bg-red-100 dark:bg-red-900/30 text-red-800 dark:text-red-300',
    MAINTENANCE: 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-800 dark:text-yellow-300',
    UNAVAILABLE: 'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-300',
  }

  const statusText = {
    AVAILABLE: 'Dostępne',
    RENTED: 'Wypożyczone',
    MAINTENANCE: 'Konserwacja',
    UNAVAILABLE: 'Niedostępne',
  }

  return (
    <Link to={`/tools/${tool.id}`} className="block h-full group">
      <Card className="h-full flex flex-col p-0 overflow-hidden border-2 border-transparent hover:border-blue-300 dark:hover:border-blue-600 transition-all duration-300">
        <div className="w-full h-[250px] overflow-hidden bg-gradient-to-br from-gray-50 to-gray-100 dark:from-slate-700 dark:to-slate-800 flex items-center justify-center flex-shrink-0 relative">
          {tool.imageUrl ? (
            <img 
              src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
              alt={tool.name}
              className="w-full h-full object-contain transition-transform duration-300 group-hover:scale-110"
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div class="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-6xl">🔧</div>'
              }}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400 dark:text-gray-500 text-6xl">🔧</div>
          )}
          <div className="absolute top-3 right-3">
            <span className={`px-3 py-1.5 rounded-full text-xs font-bold shadow-lg backdrop-blur-sm ${
              (tool.quantity === 0 || tool.status === 'UNAVAILABLE') ? statusColors.UNAVAILABLE :
              statusColors[tool.status] || statusColors.UNAVAILABLE
            }`}>
              {(tool.quantity === 0 || tool.status === 'UNAVAILABLE') ? 'Niedostępne' : (statusText[tool.status] || 'Niedostępne')}
            </span>
          </div>
        </div>
        <div className="p-6 flex flex-col flex-1 min-h-[200px] bg-white dark:bg-slate-800">
          <h3 className="text-xl font-bold text-gray-900 dark:text-gray-100 mb-2 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">{tool.name}</h3>
          <p className="text-sm text-indigo-600 dark:text-indigo-400 font-semibold mb-2 bg-indigo-50 dark:bg-indigo-900/30 px-3 py-1 rounded-full inline-block w-fit">{tool.category}</p>
          {tool.averageRating > 0 && (
            <div className="flex items-center gap-2 mb-2">
              <div className="flex items-center gap-0.5 text-yellow-500 text-sm">
                {'★'.repeat(Math.round(tool.averageRating || 0))}
                <span className="text-gray-300 dark:text-gray-600">
                  {'★'.repeat(Math.max(0, 5 - Math.round(tool.averageRating || 0)))}
                </span>
              </div>
              <span className="text-xs text-gray-500 dark:text-gray-400">
                {Number(tool.averageRating || 0).toFixed(1)} ({tool.reviewCount || 0})
              </span>
            </div>
          )}
          <p className="text-gray-600 dark:text-gray-300 mb-4 flex-1 line-clamp-3 leading-relaxed">{tool.description}</p>
          <div className="flex justify-between items-center mt-auto pt-4 border-t border-gray-100 dark:border-gray-700">
            <div>
              <span className="text-3xl font-extrabold bg-gradient-to-r from-green-600 to-emerald-600 dark:from-green-400 dark:to-emerald-400 bg-clip-text text-transparent">{tool.dailyPrice}</span>
              <span className="text-sm text-gray-500 dark:text-gray-400 ml-1">zł/dzień</span>
            </div>
            <div className="w-10 h-10 rounded-full bg-gradient-to-r from-blue-500 to-indigo-500 dark:from-blue-600 dark:to-indigo-600 flex items-center justify-center text-white font-bold group-hover:scale-110 transition-transform">
              →
            </div>
          </div>
        </div>
      </Card>
    </Link>
  )
}

export default ToolCard



