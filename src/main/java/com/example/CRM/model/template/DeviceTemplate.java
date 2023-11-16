package com.example.CRM.model.template;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"model", "brand", "isActive"})})
public class DeviceTemplate{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Model cannot be blank")
    @Size(min = 1, max = 50, message = "Model must be between 1 and 50 characters")
    private String model;

    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 1, max = 50, message = "Brand must be between 1 and 50 characters")
    private String brand;

    @NotNull(message = "Mobile indicator cannot be null")
    private boolean isMobile;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive = true;

    public DeviceTemplate(String model, String brand, boolean isMobile) {
        this.model = model;
        this.brand = brand;
        this.isMobile = isMobile;
    }
}