package com.example.CRM.model;

import com.example.CRM.model.template.DeviceTemplate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Device{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isOwned = false;

    @ManyToOne
    @JoinColumn(name = "device_template_id")
    @NotNull(message = "Device template cannot be null")
    private DeviceTemplate deviceTemplate;

    private LocalDateTime purchaseDate;

    public Device(DeviceTemplate deviceTemplate, LocalDateTime purchaseDate) {
        this.deviceTemplate = deviceTemplate;
        this.purchaseDate = purchaseDate;
    }
}
