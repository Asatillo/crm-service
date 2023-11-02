package com.example.CRM.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plan")
@Data
public class Plan {
    @Id
    private Long id;

    private String name;
    private String type;
    private String amount;
    private String duration;
    private String description;
    private Float price;

}
