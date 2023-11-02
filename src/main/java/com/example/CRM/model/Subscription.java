package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subscription")
@Data
public class Subscription {

    @Id
    private Long id;
    private boolean is_active;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
// TODO: add more fields related to the specific subscription. Like start date and end date.
}
