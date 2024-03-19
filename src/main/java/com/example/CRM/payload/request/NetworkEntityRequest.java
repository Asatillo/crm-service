package com.example.CRM.payload.request;

import com.example.CRM.model.enums.DeviceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NetworkEntityRequest {
    @NotNull(message = "Network identifier cannot be null")
//    @Pattern(regexp = AppConstants.REGEX_NETWORK_IDENTIFIER, message = "Network identifier must be a valid format. SSID of the router or phone number of the SIM card")
    private String networkIdentifier;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private Long ownerId;

    @Size(max = 50, message = "Tag must not exceed 50 characters")
    private String tag;

    private boolean isActive = true;
}
