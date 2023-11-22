package com.example.CRM.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

@Entity
@NoArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "network_entity_id")
    @NotNull(message = "Network entity cannot be null")
    private NetworkEntity networkEntity;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @NotNull(message = "Plan cannot be null")
    private Plan plan;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Subscription(NetworkEntity networkEntity, Plan plan, Device device, LocalDateTime startDate) {
        this.isActive = true;
        this.networkEntity = networkEntity;
        this.plan = plan;
        this.startDate = startDate;
        Period period = Period.parse(plan.getDuration());
        this.endDate = startDate.plusDays(period.getDays()).plusMonths(period.getMonths()).plusYears(period.getYears());
    }
}
