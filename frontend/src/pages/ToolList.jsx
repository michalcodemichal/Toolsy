import React, { useEffect, useState } from "react";
import {
  getTools,
  searchTools,
  getToolsByCategory,
  getAvailableToolsForPeriod,
} from "../services/toolService";
import ToolCard from "../components/ToolCard";
import Input from "../components/Input";
import Loading from "../components/Loading";
import Card from "../components/Card";
import Pagination from "../components/Pagination";

const ToolList = () => {
  const [tools, setTools] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [sortBy, setSortBy] = useState("");
  const [sortOrder, setSortOrder] = useState("asc");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(12);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [allCategories, setAllCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await getTools(0, 1000);
        const categories = data.content 
          ? [...new Set(data.content.map((tool) => tool.category))]
          : [...new Set(data.map((tool) => tool.category))];
        setAllCategories(categories);
      } catch (error) {
        console.error("Błąd pobierania kategorii:", error);
      }
    };
    fetchCategories();
  }, []);

  useEffect(() => {
    const fetchToolsData = async () => {
      setLoading(true);
      try {
        let response;

        if (startDate && endDate) {
          const allTools = await getAvailableToolsForPeriod(startDate, endDate);
          const toolsArray = Array.isArray(allTools) ? allTools : allTools.content || [];
          
          let filtered = toolsArray;
          if (searchTerm) {
            filtered = filtered.filter(
              (tool) =>
                tool.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                tool.description.toLowerCase().includes(searchTerm.toLowerCase())
            );
          }
          if (selectedCategory) {
            filtered = filtered.filter((tool) => tool.category === selectedCategory);
          }

          if (sortBy) {
            filtered.sort((a, b) => {
              let result = 0;
              switch (sortBy.toLowerCase()) {
                case "name":
                  result = a.name.localeCompare(b.name);
                  break;
                case "price":
                  result = parseFloat(a.dailyPrice) - parseFloat(b.dailyPrice);
                  break;
                case "category":
                  result = a.category.localeCompare(b.category);
                  break;
                default:
                  return 0;
              }
              return sortOrder === "desc" ? -result : result;
            });
          }

          const startIndex = currentPage * pageSize;
          const endIndex = startIndex + pageSize;
          const paginatedTools = filtered.slice(startIndex, endIndex);
          
          setTools(paginatedTools);
          setTotalPages(Math.ceil(filtered.length / pageSize));
          setTotalElements(filtered.length);
        } else {
          if (searchTerm) {
            response = await searchTools(searchTerm, currentPage, pageSize, sortBy, sortOrder);
          } else if (selectedCategory) {
            response = await getToolsByCategory(selectedCategory, currentPage, pageSize, sortBy, sortOrder);
          } else {
            response = await getTools(currentPage, pageSize, sortBy, sortOrder);
          }

          if (response.content) {
            setTools(response.content);
            setTotalPages(response.totalPages);
            setTotalElements(response.totalElements);
          } else {
            const toolsArray = Array.isArray(response) ? response : [];
            setTools(toolsArray);
            setTotalPages(1);
            setTotalElements(toolsArray.length);
          }
        }
      } catch (error) {
        console.error("Błąd ładowania narzędzi:", error);
        setTools([]);
        setTotalPages(0);
        setTotalElements(0);
      } finally {
        setLoading(false);
      }
    };

    fetchToolsData();
  }, [currentPage, pageSize, searchTerm, selectedCategory, sortBy, sortOrder, startDate, endDate]);

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setSelectedCategory("");
    setCurrentPage(0);
  };

  const handleCategoryChange = (e) => {
    setSelectedCategory(e.target.value);
    setSearchTerm("");
    setCurrentPage(0);
  };

  const handleSortChange = (e) => {
    setSortBy(e.target.value);
    setCurrentPage(0);
  };

  const handleSortOrderChange = (e) => {
    setSortOrder(e.target.value);
    setCurrentPage(0);
  };

  const handleDateChange = () => {
    setCurrentPage(0);
  };

  const handlePageSizeChange = (e) => {
    setPageSize(parseInt(e.target.value));
    setCurrentPage(0);
  };

  if (loading && currentPage === 0) {
    return <Loading />;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-10">
        <div className="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 dark:from-blue-700 dark:via-indigo-700 dark:to-purple-700 rounded-3xl p-8 text-white shadow-2xl mb-8">
          <h1 className="text-5xl font-extrabold mb-2 flex items-center gap-3">
            <span>🔧</span>
            Katalog narzędzi
          </h1>
          <p className="text-xl text-blue-100 dark:text-blue-200">
            Znajdź idealne narzędzie dla swoich projektów
          </p>
        </div>
        <Card className="p-6 mb-8">
          <div className="flex flex-wrap gap-4 items-end">
            <div className="w-64">
              <Input
                placeholder="🔍 Szukaj narzędzi..."
                value={searchTerm}
                onChange={handleSearchChange}
                className="mb-0"
              />
            </div>
            <div className="relative w-48">
              <input
                type="date"
                value={startDate}
                onChange={(e) => {
                  setStartDate(e.target.value);
                  if (e.target.value && endDate && e.target.value > endDate) {
                    setEndDate("");
                  }
                  handleDateChange();
                }}
                min={new Date().toISOString().split("T")[0]}
                className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100 h-10 w-full"
                onFocus={(e) => e.target.showPicker?.()}
                style={!startDate ? { color: "transparent" } : {}}
              />
              {!startDate && (
                <label className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 dark:text-gray-500 pointer-events-none select-none z-10">
                  Data rozpoczęcia
                </label>
              )}
            </div>
            <div className="relative w-48">
              <input
                type="date"
                value={endDate}
                onChange={(e) => {
                  setEndDate(e.target.value);
                  handleDateChange();
                }}
                min={startDate || new Date().toISOString().split("T")[0]}
                className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100 h-10 w-full"
                onFocus={(e) => e.target.showPicker?.()}
                style={!endDate ? { color: "transparent" } : {}}
              />
              {!endDate && (
                <label className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 dark:text-gray-500 pointer-events-none select-none z-10">
                  Data zakończenia
                </label>
              )}
            </div>
            {(startDate || endDate) && (
              <button
                onClick={() => {
                  setStartDate("");
                  setEndDate("");
                  handleDateChange();
                }}
                className="px-4 py-2 bg-red-500 hover:bg-red-600 dark:bg-red-600 dark:hover:bg-red-700 text-white rounded-xl font-semibold transition-all duration-200 shadow-lg hover:shadow-xl h-10"
                title="Wyczyść daty"
              >
                ✕
              </button>
            )}
            <select
              value={selectedCategory}
              onChange={handleCategoryChange}
              className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm h-10 shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100"
            >
              <option value="">📂 Wszystkie kategorie</option>
              {allCategories.map((category) => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </select>
            <select
              value={sortBy}
              onChange={handleSortChange}
              className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm h-10 shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100"
            >
              <option value="">🔀 Sortuj</option>
              <option value="name">Nazwa</option>
              <option value="price">Cena</option>
              <option value="category">Kategoria</option>
            </select>
            {sortBy && (
              <select
                value={sortOrder}
                onChange={handleSortOrderChange}
                className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm h-10 shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100"
              >
                <option value="asc">⬆️ Rosnąco</option>
                <option value="desc">⬇️ Malejąco</option>
              </select>
            )}
            <div className="flex items-center gap-2">
              <label className="text-sm font-medium text-gray-700 dark:text-gray-300 whitespace-nowrap flex items-center gap-2">
                <span>📄</span>
                <span>Na stronę:</span>
              </label>
              <select
                value={pageSize}
                onChange={(e) => {
                  setPageSize(Number(e.target.value));
                  setCurrentPage(0);
                }}
                className="px-4 py-3 border-2 border-gray-200 dark:border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-blue-500 dark:focus:border-blue-400 bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm shadow-sm hover:border-gray-300 dark:hover:border-gray-600 transition-all font-medium text-gray-900 dark:text-gray-100 cursor-pointer"
              >
                <option value={6}>6</option>
                <option value={12}>12</option>
                <option value={24}>24</option>
                <option value={48}>48</option>
              </select>
            </div>
          </div>
          {totalElements > 0 && (
            <div className="mt-4 text-sm text-gray-600 dark:text-gray-400">
              Wyświetlanie {currentPage * pageSize + 1} - {Math.min((currentPage + 1) * pageSize, totalElements)} z {totalElements} narzędzi
            </div>
          )}
        </Card>
      </div>

      {loading && currentPage > 0 ? (
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
            {tools.length > 0 ? (
              tools.map((tool) => <ToolCard key={tool.id} tool={tool} />)
            ) : (
              <Card className="col-span-full text-center py-16">
                <div className="text-7xl mb-4">🔍</div>
                <p className="text-gray-500 dark:text-gray-400 text-xl font-semibold">
                  Brak narzędzi do wyświetlenia
                </p>
                <p className="text-gray-400 dark:text-gray-500 mt-2">
                  Spróbuj zmienić kryteria wyszukiwania
                </p>
              </Card>
            )}
          </div>

          {totalPages > 1 && (
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={handlePageChange}
              className="mt-8"
            />
          )}
        </>
      )}
    </div>
  );
};

export default ToolList;
