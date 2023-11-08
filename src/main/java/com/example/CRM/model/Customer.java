package com.example.CRM.model;

import com.example.CRM.model.enums.Segment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private Date dob;
    @Enumerated(EnumType.STRING)
    private Segment segment;
    private LocalDateTime accCreationDate;

    public Customer(String firstName, String lastName, String phoneNumber, String email, String address, String city, Date dob, Segment segment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
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
}
