package com.toolsy.service;

import com.toolsy.messaging.RentalNotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.toolsy.config.RabbitMQConfig.RENTAL_NOTIFICATION_QUEUE;

@Service
public class NotificationService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRentalCreatedNotification(Long rentalId, Long userId, String userEmail,
                                             String userName, String toolName,
                                             java.time.LocalDate startDate, java.time.LocalDate endDate) {
        RentalNotificationMessage message = new RentalNotificationMessage(
                rentalId, userId, userEmail, userName, toolName, startDate, endDate, "CREATED"
        );
        rabbitTemplate.convertAndSend(RENTAL_NOTIFICATION_QUEUE, message);
    }

    public void sendRentalApprovedNotification(Long rentalId, Long userId, String userEmail,
                                               String userName, String toolName,
                                               java.time.LocalDate startDate, java.time.LocalDate endDate) {
        RentalNotificationMessage message = new RentalNotificationMessage(
                rentalId, userId, userEmail, userName, toolName, startDate, endDate, "APPROVED"
        );
        rabbitTemplate.convertAndSend(RENTAL_NOTIFICATION_QUEUE, message);
    }

    public void sendRentalCompletedNotification(Long rentalId, Long userId, String userEmail,
                                                String userName, String toolName,
                                                java.time.LocalDate startDate, java.time.LocalDate endDate) {
        RentalNotificationMessage message = new RentalNotificationMessage(
                rentalId, userId, userEmail, userName, toolName, startDate, endDate, "COMPLETED"
        );
        rabbitTemplate.convertAndSend(RENTAL_NOTIFICATION_QUEUE, message);
    }
}




