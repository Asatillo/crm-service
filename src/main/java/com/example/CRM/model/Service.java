package com.example.CRM.model;

import com.example.CRM.model.enums.ServiceTypes;
import com.example.CRM.utils.AppConstants;
import jakarta.persistence.*;
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
    @Column(columnDefinition = "VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;

    @NotNull(message = "Service type cannot be null")
    @Enumerated(EnumType.STRING)
    private ServiceTypes type;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private float amount;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive = true;

    @NotNull(message = "Designated device type cannot be null")
    @Pattern(regexp = AppConstants.DEVICE_TYPES_REGEX, message = "Designated device type must be one of the following: " + AppConstants.DEVICE_TYPES_REGEX)
    @Size(min = 1, max = 20, message = "Designated device type must be between 1 and 20 characters")
    private String designatedDeviceType;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    public Service(String name, ServiceTypes type, Float amount, String designatedDeviceType, Double price) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.designatedDeviceType = designatedDeviceType;
        this.price = price;
    }
}
