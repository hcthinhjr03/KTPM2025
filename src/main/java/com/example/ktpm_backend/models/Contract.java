package com.example.ktpm_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractId;
    
    @Temporal(TemporalType.DATE)
    private Date signDate;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    
    @ManyToOne
    @JoinColumn(name = "water_service_id")
    private WaterService waterService;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "contract")
    private List<Bill> bills;
}
