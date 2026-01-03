package com.toolsy.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = "rental.notifications")
    public void processNotification(RentalNotificationMessage message) {
        logger.info("Otrzymano powiadomienie o wypożyczeniu: {}", message.getNotificationType());
        logger.info("Wypożyczenie ID: {}, Użytkownik: {}, Narzędzie: {}", 
                message.getRentalId(), message.getUserName(), message.getToolName());
        
        switch (message.getNotificationType()) {
            case "CREATED":
                logger.info("Wysyłanie email do {} o utworzeniu wypożyczenia", message.getUserEmail());
                break;
            case "APPROVED":
                logger.info("Wysyłanie email do {} o zatwierdzeniu wypożyczenia", message.getUserEmail());
                break;
            case "COMPLETED":
                logger.info("Wysyłanie email do {} o zakończeniu wypożyczenia", message.getUserEmail());
                break;
        }
    }
}




