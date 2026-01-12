package com.toolsy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Żądanie utworzenia lub aktualizacji narzędzia")
public class CreateToolRequest {
    @NotBlank
    @Size(max = 200)
    @Schema(description = "Nazwa narzędzia", example = "Wiertarka udarowa", required = true)
    private String name;

    @NotBlank
    @Size(max = 1000)
    @Schema(description = "Opis narzędzia", example = "Profesjonalna wiertarka udarowa z funkcją młotkowania", required = true)
    private String description;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Kategoria narzędzia", example = "Elektryczne", required = true)
    private String category;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Cena dzienna wypożyczenia", example = "25.50", required = true)
    private BigDecimal dailyPrice;

    @NotNull
    @Schema(description = "Ilość dostępnych sztuk", example = "5", required = true)
    private Integer quantity;

    @Schema(description = "URL zdjęcia narzędzia", example = "/uploads/tool.jpg")
    private String imageUrl;

    public CreateToolRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(BigDecimal dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}







