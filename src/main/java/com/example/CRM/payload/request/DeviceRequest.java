package com.example.CRM.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeviceRequest {
    @NotNull(message = "Device template id cannot be null")
    private Long deviceTemplateId;

    private Integer amount;

    private LocalDateTime purchaseDate;
}
