package com.example.CRM.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class DeviceRequest {
    @NotNull(message = "Device template id cannot be null")
    private Long deviceTemplateId;
    @NotNull(message = "Customer id cannot be null")
    private Long customerId;
}
