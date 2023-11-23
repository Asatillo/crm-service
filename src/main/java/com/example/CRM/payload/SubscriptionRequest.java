package com.example.CRM.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class SubscriptionRequest {
    @NotNull(message = "Network entity cannot be null")
    private Long networkEntity;

    @NotNull(message = "Plan cannot be null")
    private Long planId;

    private Long deviceId;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;
}
