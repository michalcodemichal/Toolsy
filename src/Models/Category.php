<?php

class Category {
    private $db;

    public $id_kategorii;
    public $nazwa_kategorii;
    public $opis_kategorii;

    public function __construct($db_connection) {
        $this->db = $db_connection;
    }


    public function getAll() {
        $sql = "SELECT * FROM KategorieNarzędzi ORDER BY nazwa_kategorii ASC";
        try {
            $stmt = $this->db->query($sql);
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return [];
        }
    }


    public function getById($id) {
        $sql = "SELECT * FROM KategorieNarzędzi WHERE id_kategorii = :id_kategorii LIMIT 1";
        try {
            $stmt = $this->db->prepare($sql);
            $stmt->bindParam(':id_kategorii', $id, PDO::PARAM_INT);
            $stmt->execute();
            $category = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($category) {
                $this->id_kategorii = $category['id_kategorii'];
                $this->nazwa_kategorii = $category['nazwa_kategorii'];
                $this->opis_kategorii = $category['opis_kategorii'];
                return $category;
            }
            return false;
        } catch (PDOException $e) {
            return false;
        }
    }


    public function create($nazwa, $opis = null) {
        $sql = "INSERT INTO KategorieNarzędzi (nazwa_kategorii, opis_kategorii) 
                VALUES (:nazwa_kategorii, :opis_kategorii)";
        try {
            $stmt = $this->db->prepare($sql);
            $stmt->bindParam(':nazwa_kategorii', $nazwa);
            $stmt->bindParam(':opis_kategorii', $opis);

            if ($stmt->execute()) {
                $this->id_kategorii = $this->db->lastInsertId();
                return true;
            }
            return false;
        } catch (PDOException $e) {
            return false;
        }
    }


    public function update($id, $nazwa, $opis = null) {
        $sql = "UPDATE KategorieNarzędzi 
                SET nazwa_kategorii = :nazwa_kategorii, opis_kategorii = :opis_kategorii 
                WHERE id_kategorii = :id_kategorii";
        try {
            $stmt = $this->db->prepare($sql);
            $stmt->bindParam(':nazwa_kategorii', $nazwa);
            $stmt->bindParam(':opis_kategorii', $opis);
            $stmt->bindParam(':id_kategorii', $id, PDO::PARAM_INT);

            return $stmt->execute();
        } catch (PDOException $e) {
            return false;
        }
    }

    public function delete($id) {
        $sql = "DELETE FROM KategorieNarzędzi WHERE id_kategorii = :id_kategorii";
        try {
            $stmt = $this->db->prepare($sql);
            $stmt->bindParam(':id_kategorii', $id, PDO::PARAM_INT);
            return $stmt->execute();
        } catch (PDOException $e) {
            return false;
        }
    }
}
?>