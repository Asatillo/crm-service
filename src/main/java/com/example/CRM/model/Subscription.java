package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

@Entity
@Table(name = "subscription")
@NoArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean is_active;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Subscription(Customer customer, Plan plan) {
        this.is_active = true;
        this.customer = customer;
        this.plan = plan;
        this.startDate = LocalDateTime.now();
        Period period = Period.parse(plan.getDuration());
        this.endDate = startDate.plusDays(period.getDays()).plusMonths(period.getMonths()).plusYears(period.getYears());
    }
}
