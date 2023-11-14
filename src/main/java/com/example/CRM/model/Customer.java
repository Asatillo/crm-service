package com.example.CRM.model;

import com.example.CRM.model.enums.Segment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Segment segment;
    private LocalDateTime accCreationDate;

    public Customer(String firstName, String lastName, String phoneNumber, String email, String address, String city, LocalDate dob, Segment segment) {
        this.isActive = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber.replaceAll("[^0-9]", "");;
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
