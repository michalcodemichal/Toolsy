<?php


class Database {
    private static $instance = null;
    private $pdo;

    private $host = DB_HOST;
    private $port = DB_PORT;
    private $db_name = DB_NAME;
    private $username = DB_USER;
    private $password = DB_PASS;

    private function __construct() {

        if (!defined('DB_HOST')) {

            if (defined('CONFIG_PATH')) {
                 require_once CONFIG_PATH . '/database.php';
                 $this->host = DB_HOST;
                 $this->port = DB_PORT;
                 $this->db_name = DB_NAME;
                 $this->username = DB_USER;
                 $this->password = DB_PASS;
            } else {
                die("Błąd krytyczny: Brak konfiguracji bazy danych.");
            }
        }


        $dsn = 'pgsql:host=' . $this->host . ';port=' . $this->port . ';dbname=' . $this->db_name;
        try {
            $this->pdo = new PDO($dsn, $this->username, $this->password);
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        } catch (PDOException $e) {

            die("Błąd połączenia z bazą danych: " . $e->getMessage());
        }
    }

    public static function getInstance() {
        if (self::$instance === null) {
            self::$instance = new Database();
        }
        return self::$instance;
    }

    public function getConnection() {
        return $this->pdo;
    }

}
?>