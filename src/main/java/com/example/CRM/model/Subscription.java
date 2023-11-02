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

    private String plan_id;
    private String plan_type;
    private String plan_start_date;
    private String plan_end_date;
    private String plan_amount;

}
