package com.example.CRM.model.template;

import com.example.CRM.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Period;


@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"model", "brand", "storage", "color"})})
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

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Storage cannot be null")
    private Integer storage;

    @Size(min = 15, max = 255, message = "Image URL must be between 15 and 255 characters")
    private String imageUrl;

    @NotNull(message = "Color cannot be null")
    @Size(min = 1, max = 25, message = "Color must be between 1 and 25 characters")
    private String color;

    private boolean isActive = true;

    @NotNull(message = "Warranty duration cannot be null")
    @Size(min = 3, max = 10, message = "Warranty duration must be between 3 and 10 characters")
    private String warrantyDuration;

    public DeviceTemplate(String model, String brand, String deviceType, String warrantyDuration, Double price,
                          String color, Integer storage) {
        this.model = model;
        this.brand = brand;
        this.deviceType = deviceType;
        this.warrantyDuration = warrantyDuration;
        this.price = price;
        this.color = color;
        this.storage = storage;
        this.imageUrl = constructImageUrl();
    }

    private String constructImageUrl() {
        if(!deviceType.equals("MOBILE")){
            return "https://www.raneen.com/media/catalog/product/n/4/n41584596-a-1_mam7elzfeladrsb6.jpg?optimize=high&bg-color=255,255,255&fit=bounds&height=250&width=250&canvas=250:250";
        }else{
            String width = "250";
            String height = "250";
            String bg_color = "ffffff";
            return String.format("https://images.daisycon.io/mobile-device/?width=%s&height=%s&color=%s&mobile_device_brand=%s&mobile_device_model=%s+%sGB&mobile_device_color=%s",
                    width, height, bg_color, brand.toLowerCase().replace(" ", "+"), model.toLowerCase().replace(" ", "+"), storage.toString().toLowerCase().replace(" ", "+"),
                    color.toLowerCase().replace(" ", "+"));
        }
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