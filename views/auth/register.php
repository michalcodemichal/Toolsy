<?php
$errors = $GLOBALS['errors'] ?? []; 
$input = $GLOBALS['input'] ?? []; 
?>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rejestracja - Toolsy</title>
    <link rel="stylesheet" href="css/style.css"> <style>
        .email-status { 
            font-size: 0.9em; 
            margin-top: 5px; 
            min-height: 1.2em;
            font-weight: bold;
        }
        .email-available { 
            color: green !important; 
        }
        .email-taken { 
            color: red !important; 
        }

        .errors { 
            color: red; 
            border: 1px solid red; 
            padding: 10px; 
            margin-bottom: 15px; 
            background-color: #ffe0e0; 
            border-radius: 5px; 
        }
        .errors ul { 
            list-style-position: inside; 
            padding-left: 0; 
            margin-top: 5px;
            margin-bottom: 0;
        }
        .errors p {
            margin-top: 0;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .up{
            text-align:center;
        }
    </style>
</head>
<body>
    <header>
       <div class="up"><h1>Zarejestruj się w Toolsy</h1></div>
    </header>
    <main style="max-width: 500px; margin: 20px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px;">
        <?php if (!empty($errors)): ?>
            <div class="errors">
                <p>Wystąpiły błędy w formularzu:</p>
                <ul>
                    <?php foreach ($errors as $error): ?>
                        <li><?= htmlspecialchars($error) ?></li>
                    <?php endforeach; ?>
                </ul>
            </div>
        <?php endif; ?>

        <form action="index.php?action=register_process" method="POST" id="registrationForm">
            <div style="margin-bottom: 15px;">
                <label for="imie" style="display: block; margin-bottom: 5px;">Imię:</label>
                <input type="text" id="imie" name="imie" value="<?= htmlspecialchars($input['imie'] ?? '') ?>" required style="width: 98%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label for="nazwisko" style="display: block; margin-bottom: 5px;">Nazwisko:</label>
                <input type="text" id="nazwisko" name="nazwisko" value="<?= htmlspecialchars($input['nazwisko'] ?? '') ?>" required style="width: 98%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label for="email" style="display: block; margin-bottom: 5px;">Email:</label>
                <input type="email" id="email" name="email" value="<?= htmlspecialchars($input['email'] ?? '') ?>" required style="width: 98%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                <div id="emailAvailabilityStatus" class="email-status"></div>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="haslo" style="display: block; margin-bottom: 5px;">Hasło:</label>
                <input type="password" id="haslo" name="haslo" required style="width: 98%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label for="haslo_confirm" style="display: block; margin-bottom: 5px;">Powtórz hasło:</label>
                <input type="password" id="haslo_confirm" name="haslo_confirm" required style="width: 98%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div>
                <button type="submit" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">Zarejestruj się</button>
            </div>
        </form>
        <p style="margin-top: 20px; text-align: center;">Masz już konto? <a href="index.php?action=login">Zaloguj się</a></p>
    </main>


    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const emailInput = document.getElementById('email');
            const emailStatusDiv = document.getElementById('emailAvailabilityStatus');
            let debounceTimer;

            if (emailInput) {
                emailInput.addEventListener('blur', function() {
                    const email = this.value.trim();
                    

                    emailStatusDiv.textContent = '';
                    emailStatusDiv.classList.remove('email-available', 'email-taken');

                    if (email.length > 0 && isValidEmail(email)) {
                        clearTimeout(debounceTimer);
                        debounceTimer = setTimeout(() => {
                            checkEmail(email);
                        }, 300);
                    } else if (email.length > 0) {
                        emailStatusDiv.textContent = 'Niepoprawny format email.';
                        emailStatusDiv.classList.add('email-taken');
                    }
                });
            }

            function isValidEmail(email) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(email);
            }

            function checkEmail(email) {
                emailStatusDiv.textContent = 'Sprawdzam...';
                emailStatusDiv.classList.remove('email-available', 'email-taken');

                fetch(`index.php?action=check_email_availability&email=${encodeURIComponent(email)}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Problem z odpowiedzią serwera: ' + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.error) {
                            emailStatusDiv.textContent = data.error;
                            emailStatusDiv.classList.add('email-taken');
                        } else if (data.available) {
                            emailStatusDiv.textContent = 'Email jest dostępny!';
                            emailStatusDiv.classList.add('email-available');
                        } else {
                            emailStatusDiv.textContent = 'Ten email jest już zajęty.';
                            emailStatusDiv.classList.add('email-taken');
                        }
                    })
                    .catch(error => {
                        console.error('Błąd Fetch:', error);
                        emailStatusDiv.textContent = 'Nie udało się sprawdzić emaila. Błąd komunikacji.';
                        emailStatusDiv.classList.add('email-taken');
                    });
            }
        });
    </script>
</body>
</html>