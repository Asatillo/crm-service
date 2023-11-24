package com.example.CRM.payload;

import com.example.CRM.utils.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = AppConstants.REGEX_NETWORK_IDENTIFIER, message = "Network identifier must be a valid format. Public IP address of the router or phone number of the SIM card")
    private String networkIdentifier;

    @NotNull(message = "Device type cannot be null")
    @Pattern(regexp = AppConstants.DEVICE_TYPES_REGEX, message = "Device type must be one of the following: " + AppConstants.DEVICE_TYPES_REGEX)
    private String deviceType;

    @NotNull(message = "Owner cannot be null")
    private Long owner_id;

    @Size(max = 50, message = "Tag must not exceed 50 characters")
    private String tag;

    private boolean isActive = true;
}
