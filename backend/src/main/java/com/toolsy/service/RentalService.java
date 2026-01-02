package com.toolsy.service;

import com.toolsy.dto.request.CreateRentalRequest;
import com.toolsy.dto.response.RentalResponse;
import com.toolsy.model.Rental;
import com.toolsy.model.RentalStatus;
import com.toolsy.model.Tool;
import com.toolsy.model.User;
import com.toolsy.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final ToolService toolService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, ToolService toolService, 
                        UserService userService, NotificationService notificationService) {
        this.rentalRepository = rentalRepository;
        this.toolService = toolService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public RentalResponse createRental(Long userId, CreateRentalRequest request) {
        User user = userService.findById(userId);
        Tool tool = toolService.getToolEntity(request.getToolId());

        int requestedQuantity = request.getQuantity() != null ? request.getQuantity() : 1;
        if (requestedQuantity <= 0) {
            throw new RuntimeException("Ilość musi być większa od zera");
        }

        if (tool.getQuantity() < requestedQuantity) {
            throw new RuntimeException("Niewystarczająca ilość dostępnych narzędzi. Dostępne: " + tool.getQuantity());
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Data rozpoczęcia nie może być późniejsza niż data zakończenia");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data rozpoczęcia nie może być w przeszłości");
        }

        List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(
                tool,
                request.getStartDate(),
                request.getEndDate(),
                List.of(RentalStatus.PENDING, RentalStatus.ACTIVE)
        );

        long rentedQuantity = overlappingRentals.stream()
                .mapToInt(r -> r.getQuantity() != null ? r.getQuantity() : 1)
                .sum();
        
        int availableQuantity = tool.getQuantity() - (int) rentedQuantity;
        if (availableQuantity < requestedQuantity) {
            throw new RuntimeException("Brak dostępnych narzędzi w wybranym terminie. Dostępne: " + availableQuantity);
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal totalPrice = tool.getDailyPrice().multiply(BigDecimal.valueOf(days)).multiply(BigDecimal.valueOf(requestedQuantity));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setTool(tool);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setQuantity(requestedQuantity);
        rental.setTotalPrice(totalPrice);
        rental.setStatus(RentalStatus.PENDING);
        rental.setNotes(request.getNotes());

        Rental savedRental = rentalRepository.save(rental);
        
        try {
            notificationService.sendRentalCreatedNotification(
                    savedRental.getId(),
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    tool.getName(),
                    savedRental.getStartDate(),
                    savedRental.getEndDate()
            );
        } catch (Exception e) {
            System.err.println("Błąd wysyłania powiadomienia (nie blokuje wypożyczenia): " + e.getMessage());
            e.printStackTrace();
        }
        
        return mapToResponse(savedRental);
    }

    public RentalResponse approveRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new RuntimeException("Można zatwierdzić tylko wypożyczenia oczekujące");
        }

        Tool tool = rental.getTool();
        int rentalQuantity = rental.getQuantity() != null ? rental.getQuantity() : 1;
        
        if (tool.getQuantity() < rentalQuantity) {
            throw new RuntimeException("Niewystarczająca ilość dostępnych narzędzi");
        }

        tool.setQuantity(tool.getQuantity() - rentalQuantity);
        toolService.saveTool(tool);

        rental.setStatus(RentalStatus.ACTIVE);
        Rental updatedRental = rentalRepository.save(rental);
        
        notificationService.sendRentalApprovedNotification(
                updatedRental.getId(),
                updatedRental.getUser().getId(),
                updatedRental.getUser().getEmail(),
                updatedRental.getUser().getFirstName() + " " + updatedRental.getUser().getLastName(),
                updatedRental.getTool().getName(),
                updatedRental.getStartDate(),
                updatedRental.getEndDate()
        );
        
        return mapToResponse(updatedRental);
    }

    public RentalResponse completeRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new RuntimeException("Można zakończyć tylko aktywne wypożyczenia");
        }

        Tool tool = rental.getTool();
        int rentalQuantity = rental.getQuantity() != null ? rental.getQuantity() : 1;
        tool.setQuantity(tool.getQuantity() + rentalQuantity);
        toolService.saveTool(tool);

        rental.setStatus(RentalStatus.COMPLETED);
        rental.setReturnedAt(LocalDateTime.now());
        Rental updatedRental = rentalRepository.save(rental);
        
        notificationService.sendRentalCompletedNotification(
                updatedRental.getId(),
                updatedRental.getUser().getId(),
                updatedRental.getUser().getEmail(),
                updatedRental.getUser().getFirstName() + " " + updatedRental.getUser().getLastName(),
                updatedRental.getTool().getName(),
                updatedRental.getStartDate(),
                updatedRental.getEndDate()
        );
        
        return mapToResponse(updatedRental);
    }

    public RentalResponse cancelRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));

        if (rental.getStatus() == RentalStatus.COMPLETED) {
            throw new RuntimeException("Nie można anulować zakończonego wypożyczenia");
        }

        rental.setStatus(RentalStatus.CANCELLED);
        Rental updatedRental = rentalRepository.save(rental);
        return mapToResponse(updatedRental);
    }

    public List<RentalResponse> getUserRentals(Long userId) {
        User user = userService.findById(userId);
        return rentalRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));
        return mapToResponse(rental);
    }

    public void checkOverdueRentals() {
        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(
                LocalDate.now(),
                RentalStatus.ACTIVE
        );

        for (Rental rental : overdueRentals) {
            rental.setStatus(RentalStatus.OVERDUE);
            rentalRepository.save(rental);
        }
    }

    private RentalResponse mapToResponse(Rental rental) {
        RentalResponse response = new RentalResponse();
        response.setId(rental.getId());
        response.setUserId(rental.getUser().getId());
        response.setUserFirstName(rental.getUser().getFirstName());
        response.setUserLastName(rental.getUser().getLastName());
        response.setToolId(rental.getTool().getId());
        response.setToolName(rental.getTool().getName());
        response.setStartDate(rental.getStartDate());
        response.setEndDate(rental.getEndDate());
        response.setQuantity(rental.getQuantity());
        response.setTotalPrice(rental.getTotalPrice());
        response.setStatus(rental.getStatus());
        response.setNotes(rental.getNotes());
        response.setCreatedAt(rental.getCreatedAt());
        response.setUpdatedAt(rental.getUpdatedAt());
        response.setReturnedAt(rental.getReturnedAt());
        return response;
    }
}

