<?php

require_once SRC_PATH . '/Core/Database.php';
require_once SRC_PATH . '/Models/Category.php';

class CategoryController {
    private $db;
    private $categoryModel;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
        $this->categoryModel = new Category($this->db);
    }

    public function index() {
        $categories = $this->categoryModel->getAll();
        include VIEWS_PATH . '/categories/index.php';
    }

    public function createForm() {
        include VIEWS_PATH . '/categories/create.php';
    }

    public function store() {
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $nazwa = trim($_POST['nazwa_kategorii'] ?? '');
            $opis = trim($_POST['opis_kategorii'] ?? null);
            $errors = [];

            if (empty($nazwa)) {
                $errors[] = "Nazwa kategorii jest wymagana.";
            }

            if (empty($errors)) {
                if ($this->categoryModel->create($nazwa, $opis)) {
                    header('Location: index.php?action=categories_list&status=created');
                    exit;
                } else {
                    $errors[] = "Nie udało się dodać kategorii. Możliwe, że nazwa już istnieje.";
                }
            }
            include VIEWS_PATH . '/categories/create.php';
        } else {
            header('Location: index.php?action=category_create_form');
            exit;
        }
    }

    public function editForm() {
        $id_kategorii = (int)($_GET['id'] ?? 0);
        if ($id_kategorii <= 0) {
            echo "Nieprawidłowe ID kategorii.";
            return;
        }
        $category = $this->categoryModel->getById($id_kategorii);
        if (!$category) {
            echo "Kategoria nie znaleziona.";
            return;
        }
        include VIEWS_PATH . '/categories/edit.php';
    }

    public function update() {
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $id_kategorii = (int)($_POST['id_kategorii'] ?? 0);
            $nazwa = trim($_POST['nazwa_kategorii'] ?? '');
            $opis = trim($_POST['opis_kategorii'] ?? null);
            $errors = [];

            if ($id_kategorii <= 0) {
                $errors[] = "Nieprawidłowe ID kategorii.";
            }
            if (empty($nazwa)) {
                $errors[] = "Nazwa kategorii jest wymagana.";
            }

            if (empty($errors)) {
                if ($this->categoryModel->update($id_kategorii, $nazwa, $opis)) {
                    header('Location: index.php?action=categories_list&status=updated');
                    exit;
                } else {
                    $errors[] = "Nie udało się zaktualizować kategorii. Możliwe, że nowa nazwa już istnieje lub wystąpił inny błąd.";
                }
            }
            $category = ['id_kategorii' => $id_kategorii, 'nazwa_kategorii' => $nazwa, 'opis_kategorii' => $opis];
            include VIEWS_PATH . '/categories/edit.php';
        } else {
            header('Location: index.php?action=categories_list');
            exit;
        }
    }

    public function delete() {
        if ($_SERVER['REQUEST_METHOD'] === 'POST') { 
            $id_kategorii = (int)($_POST['id_kategorii'] ?? 0);
            if ($id_kategorii > 0) {
                if ($this->categoryModel->delete($id_kategorii)) {
                    header('Location: index.php?action=categories_list&status=deleted');
                    exit;
                } else {
                    header('Location: index.php?action=categories_list&status=delete_error');
                    exit;
                }
            }
        }
        header('Location: index.php?action=categories_list');
        exit;
    }
}
?>