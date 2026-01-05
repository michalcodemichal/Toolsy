package com.toolsy.messaging;

import java.io.Serializable;
import java.time.LocalDate;

public class RentalNotificationMessage implements Serializable {
    private Long rentalId;
    private Long userId;
    private String userEmail;
    private String userName;
    private String toolName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notificationType;

    public RentalNotificationMessage() {
    }

    public RentalNotificationMessage(Long rentalId, Long userId, String userEmail, String userName,
                                     String toolName, LocalDate startDate, LocalDate endDate, String notificationType) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.toolName = toolName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notificationType = notificationType;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}







