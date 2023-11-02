package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private Date dob;
    private Date accCreationDate;
    @Enumerated(EnumType.ORDINAL)
    private Segment segment;

}
