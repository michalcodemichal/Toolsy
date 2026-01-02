package com.toolsy.config;

import com.toolsy.model.*;
import com.toolsy.repository.RentalRepository;
import com.toolsy.repository.ToolRepository;
import com.toolsy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final RentalRepository rentalRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, ToolRepository toolRepository,
                          RentalRepository rentalRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.toolRepository = toolRepository;
        this.rentalRepository = rentalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        long userCount = userRepository.count();
        long toolCount = toolRepository.count();
        System.out.println("Liczba użytkowników w bazie: " + userCount);
        System.out.println("Liczba narzędzi w bazie: " + toolCount);
        
        if (userCount == 0) {
            System.out.println("Inicjalizacja danych testowych...");
            initializeData();
            System.out.println("Dane testowe zainicjalizowane pomyślnie");
            System.out.println("Liczba narzędzi po inicjalizacji: " + toolRepository.count());
        } else {
            System.out.println("Aktualizacja opisów istniejących narzędzi...");
            updateExistingTools();
            System.out.println("Aktualizacja zakończona");
        }
    }

    private void initializeData() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@toolsy.pl");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Jan");
        admin.setLastName("Kowalski");
        admin.setPhoneNumber("123456789");
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);

        List<User> users = new ArrayList<>();
        String[] firstNames = {"Anna", "Piotr", "Maria", "Tomasz", "Katarzyna", "Marcin", "Agnieszka", "Paweł", "Magdalena", "Jakub"};
        String[] lastNames = {"Nowak", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zieliński", "Szymański", "Woźniak", "Kozłowski"};

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user" + (i + 1));
            user.setEmail("user" + (i + 1) + "@toolsy.pl");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setPhoneNumber("500" + String.format("%06d", i + 1));
            user.setRole(UserRole.USER);
            user.setActive(true);
            users.add(userRepository.save(user));
        }

        String[] toolNames = {
            "Wiertarka udarowa Bosch", "Szlifierka kątowa", "Wkrętarka akumulatorowa", "Młot pneumatyczny",
            "Piła tarczowa", "Wyrzynarka", "Szlifierka oscylacyjna", "Wiertarka stołowa", "Frezarka", "Strugarka",
            "Wkrętarka udarowa", "Klucz pneumatyczny", "Wiertarka SDS", "Szlifierka pasowa", "Piła łańcuchowa",
            "Wiertarka kolumnowa", "Frezarka górnowrzecionowa", "Szlifierka do metalu", "Wiertarka ręczna", "Wkrętarka bezprzewodowa",
            "Młotowiertarka", "Szlifierka do betonu", "Piła do drewna", "Wiertarka do betonu", "Klucz dynamometryczny",
            "Wkrętarka z wiertarką", "Szlifierka do kamienia", "Piła do metalu", "Wiertarka do metalu", "Klucz nasadowy"
        };

        String[] categories = {"Elektryczne", "Pneumatyczne", "Ręczne", "Akumulatorowe", "Stacjonarne"};
        String[] descriptions = {
            "Profesjonalne narzędzie do wiercenia w betonie i innych twardych materiałach",
            "Uniwersalne narzędzie do cięcia i szlifowania różnych materiałów",
            "Wygodna wkrętarka z akumulatorem, idealna do prac domowych",
            "Mocne narzędzie do kucia i rozbiórki",
            "Precyzyjna piła do cięcia drewna i materiałów drewnopochodnych",
            "Elektryczna wyrzynarka do precyzyjnego cięcia krzywoliniowego",
            "Szlifierka oscylacyjna do prac wykończeniowych",
            "Stabilna wiertarka stołowa do precyzyjnych otworów",
            "Frezarka do obróbki drewna i tworzyw sztucznych",
            "Strugarka do wyrównywania i wygładzania powierzchni drewnianych",
            "Wkrętarka udarowa do mocnych połączeń",
            "Klucz pneumatyczny do szybkiego dokręcania",
            "Wiertarka SDS do betonu i kamienia",
            "Szlifierka pasowa do wygładzania powierzchni",
            "Piła łańcuchowa do cięcia drewna i gałęzi",
            "Wiertarka kolumnowa do precyzyjnych otworów",
            "Frezarka górnowrzecionowa do zaawansowanej obróbki",
            "Szlifierka do metalu do usuwania zadziorów",
            "Wiertarka ręczna do podstawowych prac",
            "Bezprzewodowa wkrętarka z długim czasem pracy",
            "Młotowiertarka do betonu i kamienia",
            "Szlifierka do betonu do wygładzania powierzchni",
            "Piła do drewna do precyzyjnego cięcia",
            "Wiertarka do betonu z funkcją udarową",
            "Klucz dynamometryczny do dokładnego dokręcania",
            "Wkrętarka z wiertarką do uniwersalnych zastosowań",
            "Szlifierka do kamienia do obróbki powierzchni",
            "Piła do metalu do cięcia rur i profili",
            "Wiertarka do metalu do precyzyjnych otworów",
            "Klucz nasadowy z kompletnym zestawem nasadek"
        };

        List<Tool> tools = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Tool tool = new Tool();
            tool.setName(toolNames[i]);
            tool.setDescription(descriptions[i]);
            tool.setCategory(categories[i % categories.length]);
            tool.setDailyPrice(new BigDecimal(20 + (i % 5) * 10));
            tool.setQuantity(3 + (i % 3));
            tool.setImageUrl(null);
            tool.setStatus(ToolStatus.AVAILABLE);
            tools.add(toolRepository.save(tool));
        }

        for (int i = 0; i < 10; i++) {
            Rental rental = new Rental();
            rental.setUser(users.get(i % users.size()));
            rental.setTool(tools.get(i % tools.size()));
            rental.setStartDate(LocalDate.now().plusDays(i));
            rental.setEndDate(LocalDate.now().plusDays(i + 3));
            rental.setTotalPrice(tools.get(i % tools.size()).getDailyPrice().multiply(BigDecimal.valueOf(4)));
            rental.setStatus(i % 2 == 0 ? RentalStatus.ACTIVE : RentalStatus.PENDING);
            rental.setNotes("Wypożyczenie testowe " + (i + 1));
            rentalRepository.save(rental);
        }
    }

    private void updateExistingTools() {
        String[] toolNames = {
            "Wiertarka udarowa Bosch", "Szlifierka kątowa", "Wkrętarka akumulatorowa", "Młot pneumatyczny",
            "Piła tarczowa", "Wyrzynarka", "Szlifierka oscylacyjna", "Wiertarka stołowa", "Frezarka", "Strugarka",
            "Wkrętarka udarowa", "Klucz pneumatyczny", "Wiertarka SDS", "Szlifierka pasowa", "Piła łańcuchowa",
            "Wiertarka kolumnowa", "Frezarka górnowrzecionowa", "Szlifierka do metalu", "Wiertarka ręczna", "Wkrętarka bezprzewodowa",
            "Młotowiertarka", "Szlifierka do betonu", "Piła do drewna", "Wiertarka do betonu", "Klucz dynamometryczny",
            "Wkrętarka z wiertarką", "Szlifierka do kamienia", "Piła do metalu", "Wiertarka do metalu", "Klucz nasadowy"
        };

        String[] descriptions = {
            "Profesjonalne narzędzie do wiercenia w betonie i innych twardych materiałach",
            "Uniwersalne narzędzie do cięcia i szlifowania różnych materiałów",
            "Wygodna wkrętarka z akumulatorem, idealna do prac domowych",
            "Mocne narzędzie do kucia i rozbiórki",
            "Precyzyjna piła do cięcia drewna i materiałów drewnopochodnych",
            "Elektryczna wyrzynarka do precyzyjnego cięcia krzywoliniowego",
            "Szlifierka oscylacyjna do prac wykończeniowych",
            "Stabilna wiertarka stołowa do precyzyjnych otworów",
            "Frezarka do obróbki drewna i tworzyw sztucznych",
            "Strugarka do wyrównywania i wygładzania powierzchni drewnianych",
            "Wkrętarka udarowa do mocnych połączeń",
            "Klucz pneumatyczny do szybkiego dokręcania",
            "Wiertarka SDS do betonu i kamienia",
            "Szlifierka pasowa do wygładzania powierzchni",
            "Piła łańcuchowa do cięcia drewna i gałęzi",
            "Wiertarka kolumnowa do precyzyjnych otworów",
            "Frezarka górnowrzecionowa do zaawansowanej obróbki",
            "Szlifierka do metalu do usuwania zadziorów",
            "Wiertarka ręczna do podstawowych prac",
            "Bezprzewodowa wkrętarka z długim czasem pracy",
            "Młotowiertarka do betonu i kamienia",
            "Szlifierka do betonu do wygładzania powierzchni",
            "Piła do drewna do precyzyjnego cięcia",
            "Wiertarka do betonu z funkcją udarową",
            "Klucz dynamometryczny do dokładnego dokręcania",
            "Wkrętarka z wiertarką do uniwersalnych zastosowań",
            "Szlifierka do kamienia do obróbki powierzchni",
            "Piła do metalu do cięcia rur i profili",
            "Wiertarka do metalu do precyzyjnych otworów",
            "Klucz nasadowy z kompletnym zestawem nasadek"
        };

        List<Tool> allTools = toolRepository.findAll();
        for (Tool tool : allTools) {
            String toolName = tool.getName();
            for (int i = 0; i < toolNames.length; i++) {
                if (toolNames[i].equals(toolName)) {
                    String newDescription = descriptions[i];
                    if (!newDescription.equals(tool.getDescription()) || tool.getDescription().contains("model")) {
                        tool.setDescription(newDescription);
                        toolRepository.save(tool);
                        System.out.println("Zaktualizowano: " + toolName);
                    }
                    break;
                }
            }
        }
    }
}



