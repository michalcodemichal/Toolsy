import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { getAllRentals, approveRental, completeRental } from '../services/rentalService'
import { getAllUsers, deactivateUser, activateUser } from '../services/userService'
import { getTools, createTool, updateTool, deleteTool } from '../services/toolService'
import Card from '../components/Card'
import Button from '../components/Button'
import Input from '../components/Input'
import Loading from '../components/Loading'
import './AdminPanel.css'

const AdminPanel = () => {
  const [activeTab, setActiveTab] = useState('rentals')
  const [rentals, setRentals] = useState([])
  const [users, setUsers] = useState([])
  const [tools, setTools] = useState([])
  const [loading, setLoading] = useState(true)
  const [showToolForm, setShowToolForm] = useState(false)
  const [editingTool, setEditingTool] = useState(null)
  const [toolForm, setToolForm] = useState({
    name: '',
    description: '',
    category: '',
    dailyPrice: '',
    quantity: '',
    imageUrl: ''
  })

  useEffect(() => {
    fetchData()
  }, [activeTab])

  const fetchData = async () => {
    setLoading(true)
    try {
      if (activeTab === 'rentals') {
        const data = await getAllRentals()
        setRentals(data)
      } else if (activeTab === 'users') {
        const data = await getAllUsers()
        setUsers(data)
      } else if (activeTab === 'tools') {
        const data = await getTools()
        setTools(data)
      }
    } catch (error) {
      toast.error('Błąd ładowania danych')
    } finally {
      setLoading(false)
    }
  }

  const handleApproveRental = async (id) => {
    try {
      await approveRental(id)
      toast.success('Wypożyczenie zatwierdzone')
      fetchData()
    } catch (error) {
      toast.error('Błąd zatwierdzania wypożyczenia')
    }
  }

  const handleCompleteRental = async (id) => {
    try {
      await completeRental(id)
      toast.success('Wypożyczenie zakończone')
      fetchData()
    } catch (error) {
      toast.error('Błąd kończenia wypożyczenia')
    }
  }

  const handleToggleUser = async (id, active) => {
    try {
      if (active) {
        await deactivateUser(id)
        toast.success('Użytkownik dezaktywowany')
      } else {
        await activateUser(id)
        toast.success('Użytkownik aktywowany')
      }
      fetchData()
    } catch (error) {
      toast.error('Błąd zmiany statusu użytkownika')
    }
  }

  const handleToolSubmit = async (e) => {
    e.preventDefault()
    try {
      const toolData = {
        ...toolForm,
        dailyPrice: parseFloat(toolForm.dailyPrice),
        quantity: parseInt(toolForm.quantity)
      }
      if (editingTool) {
        await updateTool(editingTool.id, toolData)
        toast.success('Narzędzie zaktualizowane')
      } else {
        await createTool(toolData)
        toast.success('Narzędzie utworzone')
      }
      setShowToolForm(false)
      setEditingTool(null)
      setToolForm({
        name: '',
        description: '',
        category: '',
        dailyPrice: '',
        quantity: '',
        imageUrl: ''
      })
      fetchData()
    } catch (error) {
      toast.error('Błąd zapisywania narzędzia')
    }
  }

  const handleDeleteTool = async (id) => {
    if (!window.confirm('Czy na pewno chcesz usunąć to narzędzie?')) {
      return
    }
    try {
      await deleteTool(id)
      toast.success('Narzędzie usunięte')
      fetchData()
    } catch (error) {
      toast.error('Błąd usuwania narzędzia')
    }
  }

  const handleEditTool = (tool) => {
    setEditingTool(tool)
    setToolForm({
      name: tool.name,
      description: tool.description,
      category: tool.category,
      dailyPrice: tool.dailyPrice.toString(),
      quantity: tool.quantity.toString(),
      imageUrl: tool.imageUrl || ''
    })
    setShowToolForm(true)
  }

  if (loading) {
    return <Loading />
  }

  return (
    <div className="admin-panel-container">
      <div className="admin-panel-header">
        <h1>Panel administracyjny</h1>
      </div>

      <div className="admin-tabs">
        <button
          className={`admin-tab ${activeTab === 'rentals' ? 'active' : ''}`}
          onClick={() => setActiveTab('rentals')}
        >
          Wypożyczenia
        </button>
        <button
          className={`admin-tab ${activeTab === 'users' ? 'active' : ''}`}
          onClick={() => setActiveTab('users')}
        >
          Użytkownicy
        </button>
        <button
          className={`admin-tab ${activeTab === 'tools' ? 'active' : ''}`}
          onClick={() => setActiveTab('tools')}
        >
          Narzędzia
        </button>
      </div>

      {activeTab === 'rentals' && (
        <div className="admin-content">
          <div className="rentals-admin-list">
            {rentals.map((rental) => (
              <Card key={rental.id} className="admin-rental-card">
                <div className="admin-rental-header">
                  <h3>{rental.toolName}</h3>
                  <span className={`rental-status rental-status-${rental.status.toLowerCase()}`}>
                    {rental.status === 'PENDING' && 'Oczekujące'}
                    {rental.status === 'ACTIVE' && 'Aktywne'}
                    {rental.status === 'COMPLETED' && 'Zakończone'}
                    {rental.status === 'CANCELLED' && 'Anulowane'}
                    {rental.status === 'OVERDUE' && 'Przeterminowane'}
                  </span>
                </div>
                <p>Użytkownik: {rental.userFirstName} {rental.userLastName}</p>
                <p>
                  {new Date(rental.startDate).toLocaleDateString('pl-PL')} -{' '}
                  {new Date(rental.endDate).toLocaleDateString('pl-PL')}
                </p>
                <p>Cena: {rental.totalPrice} zł</p>
                <div className="admin-rental-actions">
                  {rental.status === 'PENDING' && (
                    <Button onClick={() => handleApproveRental(rental.id)}>
                      Zatwierdź
                    </Button>
                  )}
                  {rental.status === 'ACTIVE' && (
                    <Button onClick={() => handleCompleteRental(rental.id)}>
                      Zakończ
                    </Button>
                  )}
                </div>
              </Card>
            ))}
          </div>
        </div>
      )}

      {activeTab === 'users' && (
        <div className="admin-content">
          <div className="users-admin-list">
            {users.map((user) => (
              <Card key={user.id} className="admin-user-card">
                <div className="admin-user-info">
                  <h3>{user.firstName} {user.lastName}</h3>
                  <p>@{user.username}</p>
                  <p>{user.email}</p>
                  <p>Rola: {user.role}</p>
                </div>
                <Button
                  variant={user.active ? 'danger' : 'success'}
                  onClick={() => handleToggleUser(user.id, user.active)}
                >
                  {user.active ? 'Dezaktywuj' : 'Aktywuj'}
                </Button>
              </Card>
            ))}
          </div>
        </div>
      )}

      {activeTab === 'tools' && (
        <div className="admin-content">
          <div className="tools-admin-header">
            <h2>Zarządzanie narzędziami</h2>
            <Button onClick={() => {
              setShowToolForm(true)
              setEditingTool(null)
              setToolForm({
                name: '',
                description: '',
                category: '',
                dailyPrice: '',
                quantity: '',
                imageUrl: ''
              })
            }}>
              Dodaj narzędzie
            </Button>
          </div>

          {showToolForm && (
            <Card className="tool-form-card">
              <h3>{editingTool ? 'Edytuj narzędzie' : 'Nowe narzędzie'}</h3>
              <form onSubmit={handleToolSubmit}>
                <Input
                  label="Nazwa"
                  value={toolForm.name}
                  onChange={(e) => setToolForm({ ...toolForm, name: e.target.value })}
                  required
                />
                <Input
                  label="Opis"
                  type="textarea"
                  value={toolForm.description}
                  onChange={(e) => setToolForm({ ...toolForm, description: e.target.value })}
                  required
                />
                <Input
                  label="Kategoria"
                  value={toolForm.category}
                  onChange={(e) => setToolForm({ ...toolForm, category: e.target.value })}
                  required
                />
                <Input
                  label="Cena dzienna"
                  type="number"
                  step="0.01"
                  value={toolForm.dailyPrice}
                  onChange={(e) => setToolForm({ ...toolForm, dailyPrice: e.target.value })}
                  required
                />
                <Input
                  label="Ilość"
                  type="number"
                  value={toolForm.quantity}
                  onChange={(e) => setToolForm({ ...toolForm, quantity: e.target.value })}
                  required
                />
                <Input
                  label="URL zdjęcia"
                  value={toolForm.imageUrl}
                  onChange={(e) => setToolForm({ ...toolForm, imageUrl: e.target.value })}
                />
                <div className="tool-form-actions">
                  <Button type="submit">
                    {editingTool ? 'Zapisz zmiany' : 'Utwórz'}
                  </Button>
                  <Button
                    type="button"
                    variant="secondary"
                    onClick={() => {
                      setShowToolForm(false)
                      setEditingTool(null)
                    }}
                  >
                    Anuluj
                  </Button>
                </div>
              </form>
            </Card>
          )}

          <div className="tools-admin-list">
            {tools.map((tool) => (
              <Card key={tool.id} className="admin-tool-card">
                <div className="admin-tool-info">
                  <h3>{tool.name}</h3>
                  <p>{tool.category}</p>
                  <p>{tool.dailyPrice} zł/dzień</p>
                  <p>Ilość: {tool.quantity}</p>
                </div>
                <div className="admin-tool-actions">
                  <Button variant="secondary" onClick={() => handleEditTool(tool)}>
                    Edytuj
                  </Button>
                  <Button variant="danger" onClick={() => handleDeleteTool(tool.id)}>
                    Usuń
                  </Button>
                </div>
              </Card>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

export default AdminPanel

