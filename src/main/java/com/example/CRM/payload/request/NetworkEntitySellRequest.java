package com.example.CRM.payload.request;

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
public class NetworkEntitySellRequest {
    @NotNull(message = "Owner ID cannot be null")
    private Long ownerId;

    @Size(max = 50, message = "Tag must not exceed 50 characters")
    private String tag;
}
