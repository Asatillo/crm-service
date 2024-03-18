package com.example.CRM.model;

import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.enums.ServiceTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Enumerated(EnumType.STRING)
    private DeviceType designatedDeviceType;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    public Service(String name, ServiceTypes type, Float amount, DeviceType designatedDeviceType, Double price) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.designatedDeviceType = designatedDeviceType;
        this.price = price;
    }
}
