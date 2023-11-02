package com.example.CRM.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Table(name = "customer")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String email;
    @NonNull
    private String address;
    @NonNull
    private String city;
    @NonNull
    private Date dob;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Segment segment;
    @CreationTimestamp
    private Date accCreationDate;
}
