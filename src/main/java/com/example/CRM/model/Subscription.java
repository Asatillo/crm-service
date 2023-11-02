package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.util.Date;

@Entity
@Table(name = "subscription")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private boolean is_active;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @NonNull
    @CreationTimestamp
    private Date startDate;

//    TODO: Automatically calculates during the creation of the subscription. The value is calculated by adding the duration of the plan to the start date of the subscription.
//    @NonNull
//    @Formula("DATE_ADD(start_date, INTERVAL plan.duration DAY)")
//    private Date endDate;

}
