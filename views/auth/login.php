<?php

$errors = $GLOBALS['errors'] ?? [];
$email_value = $GLOBALS['email_value'] ?? ''; 
$success_message = ''; 


if (isset($_GET['status']) && $_GET['status'] === 'registered') {
    $success_message = "Rejestracja zakończona pomyślnie! Możesz się teraz zalogować.";
}

?>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logowanie - Toolsy</title>
    <link rel="stylesheet" href="css/style.css">
    </head>
<body>
    <main class="container"> <div class="page-header"> <h1>Zaloguj się do Toolsy</h1>
        </div>

        <?php if (!empty($success_message)): ?>
            <div class="alert alert-success">
                <?= htmlspecialchars($success_message) ?>
            </div>
        <?php endif; ?>

        <?php if (!empty($errors)): ?>
            <div class="alert alert-danger">
                <p>Wystąpiły błędy:</p>
                <ul>
                    <?php foreach ($errors as $error): ?>
                        <li><?= htmlspecialchars($error) ?></li>
                    <?php endforeach; ?>
                </ul>
            </div>
        <?php endif; ?>

        <div style="max-width: 500px; margin: 20px auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; background-color: #fff;">
            <form action="index.php?action=login_process" method="POST">
                <div>
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required value="<?= htmlspecialchars($email_value) ?>">
                </div>
                <div>
                    <label for="haslo">Hasło:</label>
                    <input type="password" id="haslo" name="haslo" required>
                </div>
                <div style="text-align: center;"> <button type="submit">Zaloguj się</button>
                </div>
            </form>
            <p style="margin-top: 20px; text-align:center;">Nie masz konta? <a href="index.php?action=register">Zarejestruj się</a></p>
            </div>
    </main>

    </body>
</html>