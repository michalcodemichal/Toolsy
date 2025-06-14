<?php


if (!defined('SRC_PATH')) {
    define('SRC_PATH', dirname(__DIR__)); 
}

require_once SRC_PATH . '/Core/Database.php';
require_once SRC_PATH . '/Models/User.php';

class AuthController {

    private $db;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
    }

    public function showRegistrationForm() {
        $errors = $GLOBALS['errors'] ?? []; 
        $input = $GLOBALS['input'] ?? [];
        include VIEWS_PATH . '/auth/register.php';
    }

    public function processRegistration() {
        $errors = []; 
        $input = [
            'imie' => trim($_POST['imie'] ?? ''),
            'nazwisko' => trim($_POST['nazwisko'] ?? ''),
            'email' => trim($_POST['email'] ?? '')
        ];

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $imie = $input['imie'];
            $nazwisko = $input['nazwisko'];
            $email = $input['email'];
            $haslo = $_POST['haslo'] ?? '';
            $haslo_confirm = $_POST['haslo_confirm'] ?? '';

            if (empty($imie)) $errors[] = "Imię jest wymagane.";
            if (empty($nazwisko)) $errors[] = "Nazwisko jest wymagane.";
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) $errors[] = "Niepoprawny format email.";
            if (empty($haslo)) $errors[] = "Hasło jest wymagane.";
            if (strlen($haslo) < 6) $errors[] = "Hasło musi mieć co najmniej 6 znaków.";
            if ($haslo !== $haslo_confirm) $errors[] = "Hasła nie są takie same.";


            if (empty($errors)) {
                $user = new User($this->db);

                if ($user->findByEmail($email)) {
                    $errors[] = "Ten adres email jest już zajęty.";
                } else {
                    if ($user->create($imie, $nazwisko, $email, $haslo)) {
                        header('Location: index.php?action=login&status=registered');
                        exit;
                    } else {
                        $errors[] = "Nie udało się zarejestrować użytkownika. Spróbuj ponownie.";
                    }
                }
            }
        }
        $GLOBALS['errors'] = $errors;
        $GLOBALS['input'] = $input; 
        include VIEWS_PATH . '/auth/register.php';
    }

    public function showLoginForm() {
        $errors = $GLOBALS['errors'] ?? []; 
        $email_value = $GLOBALS['email_value'] ?? ''; 
        $success_message = ''; 

        if (isset($_GET['status']) && $_GET['status'] === 'registered') {
            $success_message = "Rejestracja zakończona pomyślnie! Możesz się teraz zalogować.";
        }

        include VIEWS_PATH . '/auth/login.php';
    }

    public function processLogin() {
        $errors = [];
        $email_value = trim($_POST['email'] ?? ''); 

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $email = $email_value;
            $haslo = $_POST['haslo'] ?? '';

            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                $errors[] = "Niepoprawny format email.";
            }
            if (empty($haslo)) {
                $errors[] = "Hasło jest wymagane.";
            }

            if (empty($errors)) {
                $userModel = new User($this->db);
                $loggedInUser = $userModel->authenticate($email, $haslo);

                if ($loggedInUser) {
                    $_SESSION['user_id'] = $loggedInUser['id_uzytkownika'];
                    $_SESSION['user_email'] = $loggedInUser['email'];
                    $_SESSION['user_role'] = $loggedInUser['rola'];
                    $_SESSION['user_imie'] = $loggedInUser['imie'];

                    header('Location: index.php?action=home&status=loggedin');
                    exit;
                } else {
                    $errors[] = "Nieprawidłowy email lub hasło.";
                }
            }
        }
        $GLOBALS['errors'] = $errors;
        $GLOBALS['email_value'] = $email_value; 
        include VIEWS_PATH . '/auth/login.php';
    }

    public function logout() {
        if (session_status() == PHP_SESSION_NONE) {
            session_start();
        }
        session_destroy();
        header('Location: index.php?action=home&status=loggedout');
        exit;
    }

    public static function checkAdmin() {
        if (session_status() == PHP_SESSION_NONE) {
            session_start(); 
        }

        if (!isset($_SESSION['user_id']) || !isset($_SESSION['user_role']) || $_SESSION['user_role'] !== 'admin') {
            http_response_code(403); 
            echo "<!DOCTYPE html><html lang='pl'><head><meta charset='UTF-8'><title>Brak uprawnień</title><link rel='stylesheet' href='css/style.css'></head><body>"; 
            echo "<div style='text-align:center; padding: 50px; font-family: sans-serif;'>"; 
            echo "<h1>Brak uprawnień (403 Forbidden)</h1>";
            echo "<p>Nie masz uprawnień do dostępu do tej strony. Tylko administratorzy mogą tu wejść.</p>";
            echo '<p><a href="index.php?action=home" style="color: #007bff; text-decoration: none;">Powrót na stronę główną</a></p>'; 
            echo "</div></body></html>";
            exit; 
        }
        return true;
    }


    public function checkEmailAvailability() {
        header('Content-Type: application/json');

        $email = trim($_GET['email'] ?? '');
        $response = ['available' => false];

        if (!empty($email) && filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $userModel = new User($this->db);
            if ($userModel->findByEmail($email)) {
                $response['available'] = false;
            } else {
                $response['available'] = true;
            }
        } else {
            $response['error'] = 'Nieprawidłowy format email lub brak emaila.';
        }

        echo json_encode($response);
        exit;
    }
}
?>