<?php
?>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <title>Nasze Narzędzia <?= isset($selected_category_name) ? '- ' . htmlspecialchars($selected_category_name) : '' ?> - Toolsy</title>
    <link rel="stylesheet" href="css/style.css">
    <style>

        .category-filters {
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
            text-align: center;
        }
        .category-filters h3 {
            margin-bottom: 10px;
            font-size: 1.2em;
            color: #555;
        }
        .category-filters a, .category-filters span {
            display: inline-block;
            padding: 8px 15px;
            margin: 5px;
            border-radius: 20px;
            text-decoration: none;
            background-color: #e9ecef;
            color: #495057;
            font-size: 0.9em;
            transition: background-color 0.2s, color 0.2s;
        }
        .category-filters a:hover {
            background-color: #007bff;
            color: white;
        }
        .category-filters a.active-filter {
            background-color: #007bff;
            color: white;
            font-weight: bold;
        }

      

        .tool-card-content {
            padding: 15px; 
            display: flex;
            flex-direction: column;
            flex-grow: 1; 
        }

        .tool-card-main-info {
            flex-grow: 1;
        }
        
        .tool-card-bottom {
            margin-top: 10px;
        }

        .tool-card .details-button {
            display: block; 
            width: 100%;    
            padding: 8px 10px;
            font-size: 0.95em;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
    <main class="container">
        <div class="page-header">
            <h1>Nasze Narzędzia <?= isset($selected_category_name) ? '- ' . htmlspecialchars($selected_category_name) : '' ?></h1>
        </div>

        <div class="category-filters">
            <h3>Filtruj po kategorii:</h3>
            <a href="index.php?action=tools_public_list" class="<?= !isset($_GET['category_id']) || empty($_GET['category_id']) ? 'active-filter' : '' ?>">Wszystkie Kategorie</a>
            <?php if (isset($categories) && !empty($categories)): ?>
                <?php foreach ($categories as $category): ?>
                    <a href="index.php?action=tools_public_list&category_id=<?= $category['id_kategorii'] ?>"
                       class="<?= (isset($_GET['category_id']) && $_GET['category_id'] == $category['id_kategorii']) ? 'active-filter' : '' ?>">
                        <?= htmlspecialchars($category['nazwa_kategorii']) ?>
                    </a>
                <?php endforeach; ?>
            <?php endif; ?>
        </div>

        <div class="tool-grid">
            <?php if (isset($tools) && !empty($tools)): ?>
                <?php foreach ($tools as $tool): ?>
                    <div class="tool-card"> <?php if (!empty($tool['zdjecie_url'])): ?>
                            <img src="<?= htmlspecialchars($tool['zdjecie_url']) ?>" alt="<?= htmlspecialchars($tool['nazwa_narzedzia']) ?>">
                        <?php else: ?>
                            <img src="https://via.placeholder.com/280x200.png?text=Brak+Zdjecia" alt="Brak zdjęcia">
                        <?php endif; ?>
                        
                        <div class="tool-card-content"> <div class="tool-card-main-info">
                                <h3><?= htmlspecialchars($tool['nazwa_narzedzia']) ?></h3>
                                <p class="category">Kategoria: <?= htmlspecialchars($tool['nazwa_kategorii'] ?? 'N/A') ?></p>
                            </div>
                            
                            <div class="tool-card-bottom">
                                <p class="price"><?= htmlspecialchars(number_format(floatval($tool['cena_za_dobe']), 2, ',', ' ')) ?> zł / doba</p>
                                <p class="availability">
                                    Dostępność: 
                                    <span class="<?= $tool['dostepnosc'] ? 'available' : 'unavailable' ?>">
                                        <?= $tool['dostepnosc'] ? 'Dostępne' : 'Niedostępne' ?>
                                    </span>
                                </p>
                                <a href="index.php?action=show_tool_details&id=<?= $tool['id_narzedzia'] ?>" class="button-link details-button">Zobacz szczegóły</a>
                            </div>
                        </div>
                    </div>
                <?php endforeach; ?>
            <?php else: ?>
                <p class="alert alert-info">Brak narzędzi pasujących do wybranych kryteriów.</p>
            <?php endif; ?>
        </div>
    </main>

    </body>
</html>