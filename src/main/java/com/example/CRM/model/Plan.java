package com.example.CRM.model;

import com.example.CRM.model.enums.DeviceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO: plan cannot have two service with same type
// TODO: there cannot be 2 same plans
@Entity
@NoArgsConstructor
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @Column(columnDefinition = "VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;

    @NotBlank(message = "Duration cannot be blank")
    @Size(min = 3, max = 10, message = "Duration must be between 1 and 10 characters")
    private String duration;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 255, message = "Description must be between 1 and 255 characters")
    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @Enumerated(EnumType.STRING)
    private DeviceType designatedDeviceType;

    @ManyToMany
    @JoinColumn(name = "plan_id")
    // TODO: change to set
    private List<Service> services;

    // field for the allowed segment types
//    @NotNull(message = "Segment cannot be null")
//    private String segment;

    public Plan(String name, String duration, String description, Double price, List<Service> services, DeviceType designatedDeviceType) {
        this.isActive = true;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.services = services;
        this.designatedDeviceType = designatedDeviceType;
    }
}
