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
    @NotNull
    private long networkEntity;
    @NotNull
    private long planId;
    @NotNull
    private long deviceId;
    @NotNull
    private LocalDateTime startDate;
}
