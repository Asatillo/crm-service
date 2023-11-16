package com.example.CRM.model;

import com.example.CRM.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 25, message = "First name must be between 1 and 25 characters long")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 25, message = "Last name must be between 1 and 25 characters long")
    private String lastName;

    @NotBlank(message = "Phone number cannot be blank")
    @Column(unique = true)
    @Size(min = 11,max = 16, message = "Phone number must be between 11 and 16 characters long")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    @Size(max = 50)
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100)
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 30)
    private String city;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;

    @NotNull(message = "Segment cannot be null")
    @Pattern(regexp = AppConstants.SEGMENT_TYPES_REGEX, message = "Segment must be one of the following: " + AppConstants.SEGMENT_TYPES_REGEX)
    private String segment;

    @Column(updatable = false)
    private LocalDateTime accCreationDate;

    public Customer(String firstName, String lastName, String phoneNumber, String email, String address, String city, LocalDate dob, String segment) {
        this.isActive = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        this.email = email;
        this.address = address;
        this.city = city;
        this.dob = dob;
        this.segment = segment;
        this.accCreationDate = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isInCapital(){
        return city.equals("Budapest");
    }

    @JsonIgnore
    public void processPhoneNumber(){
        this.phoneNumber = this.phoneNumber.replaceAll("[^0-9]", "");
    }
}
