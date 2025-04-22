package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "water_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;
    
    private String serviceName;
    private String description;
    private String unit;
    
    @OneToMany(mappedBy = "waterService", cascade = CascadeType.ALL)
    private List<PriceRate> priceRates;
    
    @JsonIgnore
    @OneToMany(mappedBy = "waterService")
    private List<Contract> contracts;
    
}