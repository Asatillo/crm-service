package com.example.CRM.model;

import com.example.CRM.utils.AppConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Package type cannot be null")
    @Pattern(regexp = AppConstants.PACKAGE_TYPES_REGEX,
            message = "Package type must be one of the following: " + AppConstants.PACKAGE_TYPES_REGEX)
    private String packageType;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Integer amount;

    @NotBlank(message = "Duration cannot be blank")
    @Size(min = 3, max = 10, message = "Duration must be between 1 and 10 characters")
    private String duration;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 250, message = "Description must be between 1 and 250 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    public Plan(String name, String packageType, Integer amount, String duration, String description, Double price) {
        this.isActive = true;
        this.name = name;
        this.packageType = packageType;
        this.amount = amount;
        this.duration = duration;
        this.description = description;
        this.price = price;
    }
}
