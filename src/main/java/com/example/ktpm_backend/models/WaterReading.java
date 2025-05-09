package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    
    // Đánh dấu trường này là không insertable/updatable vì nó là STORED GENERATED column
    @Column(name = "consumption", insertable = false, updatable = false)
    private Double consumption;
    
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    
    @JsonIgnore
    @OneToOne(mappedBy = "waterReading")
    private Bill bill;
}