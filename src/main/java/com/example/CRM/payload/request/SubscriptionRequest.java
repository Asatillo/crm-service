package com.example.CRM.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
    @NotNull(message = "Network entity cannot be null")
    private Long networkEntity;

    @NotNull(message = "Plan cannot be null")
    private Long planId;

    private Long promotionId;

    private LocalDate startDate = LocalDate.now();
}
