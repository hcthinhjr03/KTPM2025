package com.example.ktpm_backend.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "price_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rateId;
    
    private Double fromAmount;
    private Double toAmount;
    private Double unitPrice;
    
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;
    
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_id")
    private WaterService waterService;
}
