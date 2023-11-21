package com.example.CRM.model;

import com.example.CRM.utils.AppConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Service type cannot be null")
    @Pattern(regexp = AppConstants.SERVICE_TYPES_REGEX,
            message = "Package type must be one of the following: " + AppConstants.SERVICE_TYPES_REGEX)
    private String type;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private float amount;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive;

    public Service(String name, String type, Float amount) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.isActive = true;
    }
}
