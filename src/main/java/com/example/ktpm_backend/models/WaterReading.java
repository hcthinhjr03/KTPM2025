package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "water_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer readingId;
    
    private Double previousReading;
    private Double currentReading;
    
    @Temporal(TemporalType.DATE)
    private Date readingDate;
    
    private Double consumption;
    
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    
    @OneToOne(mappedBy = "waterReading")
    private Bill bill;
}
