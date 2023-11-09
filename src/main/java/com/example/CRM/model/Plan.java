package com.example.CRM.model;

import com.example.CRM.model.enums.Package;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String name;
    @Enumerated(EnumType.STRING)
    private Package packageType;
    private Integer amount;
    private String duration;
    private String description;
    private Double price;

    public Plan(String name, Package packageType, Integer amount, String duration, String description, Double price) {
        this.isActive = true;
        this.name = name;
        this.packageType = packageType;
        this.amount = amount;
        this.duration = duration;
        this.description = description;
        this.price = price;
    }
}
