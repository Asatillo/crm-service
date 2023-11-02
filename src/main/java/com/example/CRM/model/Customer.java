package com.example.CRM.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    private Long id;
    private String f_name;
    private String l_name;
    private String phone_number;
    private String email;
    private String address;
    private String city;
    private Date dob;
    private Date acc_creation_date;
    private String segment;

}
