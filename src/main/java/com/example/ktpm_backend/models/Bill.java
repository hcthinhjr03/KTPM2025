package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;
    
    @Temporal(TemporalType.DATE)
    private Date billDate;
    
    private Double amount;
    private String status;
    
    @OneToOne
    @JoinColumn(name = "water_reading_id")
    private WaterReading waterReading;
    
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
