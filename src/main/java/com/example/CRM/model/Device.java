package com.example.CRM.model;

import com.example.CRM.model.template.DeviceTemplate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @ManyToOne
    @JoinColumn(name = "device_template_id")
    @NotNull(message = "Device template cannot be null")
    private DeviceTemplate deviceTemplate;

    private LocalDateTime addedAt = LocalDateTime.now();

    private LocalDateTime purchaseDate;

    public boolean isOwned(){
        return owner != null;
    }

    public Device(DeviceTemplate deviceTemplate, LocalDateTime purchaseDate, Customer owner) {
        this.deviceTemplate = deviceTemplate;
        this.purchaseDate = purchaseDate;
        this.owner = owner;
    }

    public Device(DeviceTemplate deviceTemplate) {
        this.deviceTemplate = deviceTemplate;
    }
}
