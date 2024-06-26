package com.example.CRM.model;

import com.example.CRM.model.enums.DeviceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class NetworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Network identifier cannot be null")
//    @Pattern(regexp = AppConstants.REGEX_NETWORK_IDENTIFIER, message = "Network identifier must be a valid format. SSID of the router or phone number of the SIM card")
    @Size(max = 20, message = "Network identifier must not exceed 20 characters")
    private String networkIdentifier;

    @NotNull(message = "Device type cannot be null")
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Size(max = 50, message = "Tag must not exceed 50 characters")
    private String tag;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive;

    public boolean isMobile() {
        return deviceType.equals(DeviceType.MOBILE);
    }

    public boolean isRouter() {
        return deviceType.equals(DeviceType.ROUTER);
    }

    public NetworkEntity(String networkIdentifier, DeviceType deviceType, Customer owner, String tag) {
        this.networkIdentifier = networkIdentifier;
        this.deviceType = deviceType;
        this.owner = owner;
        this.tag = tag;
        this.isActive = true;
        if (isMobile()){
            processPhoneNumber();
        }
    }

    public NetworkEntity(String networkIdentifier, DeviceType deviceType, String tag) {
        this.networkIdentifier = networkIdentifier;
        this.deviceType = deviceType;
        this.owner = null;
        this.tag = tag;
        this.isActive = true;
        if (isMobile()){
            processPhoneNumber();
        }
    }

    @JsonIgnore
    public void processPhoneNumber(){
        this.networkIdentifier = this.networkIdentifier.replaceAll("[^0-9]", "");
    }
}
