package com.example.CRM.model;

import com.example.CRM.model.template.DeviceTemplate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Data
@NoArgsConstructor
public class Device{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_template_id")
    @NotNull(message = "Device template cannot be null")
    private DeviceTemplate deviceTemplate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull(message = "Customer cannot be null")
    private Customer owner;

    @NotNull(message = "Purchase date cannot be null")
    private LocalDateTime purchaseDate;

    @NotNull(message = "Warranty date cannot be null")
    private LocalDateTime warrantyExpirationDate;

    public Device(DeviceTemplate deviceTemplate, Customer owner, LocalDateTime purchaseDate) {
        this.deviceTemplate = deviceTemplate;
        this.owner = owner;
        this.purchaseDate = purchaseDate;
        Period period = deviceTemplate.getWarrantyDurationPeriod();
        this.warrantyExpirationDate = purchaseDate.plus(period);
    }
}
