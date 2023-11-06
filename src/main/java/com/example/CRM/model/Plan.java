package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Package packageType;
    @NonNull
    private Integer amount;
    @NonNull
    private String duration;
    @NonNull
    private String description;
    @NonNull
    private Double price;

}
