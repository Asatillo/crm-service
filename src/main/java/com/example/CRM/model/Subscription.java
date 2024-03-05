package com.example.CRM.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@NoArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "network_entity_id")
    @NotNull(message = "Network entity cannot be null")
    private NetworkEntity networkEntity;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @NotNull(message = "Plan cannot be null")
    private Plan plan;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;
    private LocalDate endDate;

    public Subscription(NetworkEntity networkEntity, Plan plan, LocalDate startDate) {
        this.networkEntity = networkEntity;
        this.plan = plan;
        this.startDate = startDate;
        assignEndDate();
    }

    private void assignEndDate() {
        Period period = Period.parse(this.plan.getDuration());
        endDate = startDate.plusDays(period.getDays()).plusMonths(period.getMonths()).plusYears(period.getYears());
    }

}
