import React from 'react'
import { Link } from 'react-router-dom'
import Card from './Card'
import './ToolCard.css'

const ToolCard = ({ tool }) => {
  return (
    <Link to={`/tools/${tool.id}`} className="tool-card-link">
      <Card className="tool-card">
        <div className="tool-card-image">
          {tool.imageUrl ? (
            <img 
              src={tool.imageUrl.startsWith('http') ? tool.imageUrl : `http://localhost:8080${tool.imageUrl}`} 
              alt={tool.name}
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div class="tool-card-placeholder">Brak zdjęcia</div>'
              }}
            />
          ) : (
            <div className="tool-card-placeholder">Brak zdjęcia</div>
          )}
        </div>
        <div className="tool-card-content">
          <h3 className="tool-card-title">{tool.name}</h3>
          <p className="tool-card-category">{tool.category}</p>
          <p className="tool-card-description">{tool.description}</p>
          <div className="tool-card-footer">
            <span className="tool-card-price">{tool.dailyPrice} zł/dzień</span>
            <span className={`tool-card-status tool-card-status-${tool.status.toLowerCase()}`}>
              {tool.status === 'AVAILABLE' ? 'Dostępne' : 
               tool.status === 'RENTED' ? 'Wypożyczone' : 
               tool.status === 'MAINTENANCE' ? 'Konserwacja' : 'Niedostępne'}
            </span>
          </div>
        </div>
      </Card>
    </Link>
  )
}

export default ToolCard



