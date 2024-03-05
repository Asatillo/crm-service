package com.example.CRM.model;

import com.example.CRM.model.enums.SegmentTypes;
import com.example.CRM.utils.AppConstants;
import com.example.CRM.utils.AppUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Active indicator cannot be null")
    private boolean isActive = true;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 25, message = "First name must be between 1 and 25 characters long")
    @Column(columnDefinition = "VARCHAR(25) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 25, message = "Last name must be between 1 and 25 characters long")
    @Column(columnDefinition = "VARCHAR(25) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    @Size(max = 50, message = "Email must be between 1 and 50 characters long")
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100, message = "Address must be between 1 and 100 characters long")
    @Column(columnDefinition = "VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must be between 1 and 50 characters long")
    @Column(columnDefinition = "VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String city;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;

    @NotNull(message = "Segment cannot be null")
    @Enumerated(EnumType.STRING)
    private SegmentTypes segment;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime accCreationDate;

    public Customer(String firstName, String lastName, String email, String address, String city, LocalDate dob, SegmentTypes segment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.city = city;
        AppUtils.validateDOB(dob);
        this.dob = dob;
        this.segment = segment;
    }

    public boolean isWiredInternetAvailable(){
        return AppConstants.CITIES_WITH_WIRED_INTERNET.contains(city);
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
