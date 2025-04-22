package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;
    
    private String fullName;
    private String identityCard;
    private String phoneNumber;
    private String email;
    private String address;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Apartment> apartments;
    
    @OneToMany(mappedBy = "customer")
    private List<Contract> contracts;
}
