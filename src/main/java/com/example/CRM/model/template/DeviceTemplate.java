package com.example.CRM.model.template;

import com.example.CRM.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Period;


@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"model", "brand"})})
public class DeviceTemplate{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Model cannot be blank")
    @Size(min = 1, max = 25, message = "Model must be between 1 and 50 characters")
    private String model;

    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 1, max = 25, message = "Brand must be between 1 and 50 characters")
    private String brand;

    @NotNull(message = "Device type cannot be null")
    @Pattern(regexp = AppConstants.DEVICE_TYPES_REGEX, message = "Device type must be one of the following: " + AppConstants.DEVICE_TYPES_REGEX)
    @Size(max = 20, message = "Device type must not exceed 20 characters")
    private String deviceType;

    private boolean isActive = true;

    @NotNull(message = "Warranty duration cannot be null")
    @Size(min = 3, max = 10, message = "Warranty duration must be between 3 and 10 characters")
    private String warrantyDuration;

    public DeviceTemplate(String model, String brand, String deviceType, String warrantyDuration) {
        this.model = model;
        this.brand = brand;
        this.deviceType = deviceType;
        this.warrantyDuration = warrantyDuration;
    }

    @JsonIgnore
    public Period getWarrantyDurationPeriod() {
        return Period.parse(warrantyDuration);
    }

    public boolean isMobile() {
        return deviceType.equals("MOBILE");
    }

    public boolean isRouter() {
        return deviceType.equals("ROUTER");
    }
}