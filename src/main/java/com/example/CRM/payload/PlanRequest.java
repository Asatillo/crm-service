package com.example.CRM.payload;

import com.example.CRM.utils.AppConstants;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
public class PlanRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotBlank(message = "Duration cannot be blank")
    @Size(min = 3, max = 10, message = "Duration must be between 1 and 10 characters")
    private String duration;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 250, message = "Description must be between 1 and 250 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Designated device type cannot be null")
    @Pattern(regexp = AppConstants.DEVICE_TYPES_REGEX, message = "Designated device type must be one of the following: " + AppConstants.DEVICE_TYPES_REGEX)
    private String designatedDeviceType;

    @NotNull(message = "Services cannot be null")
    private List<Long> services;
}
