package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.WaterReading;
import com.example.ktpm_backend.repositories.ContractRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;
    
    @Autowired
    private WaterReadingService waterReadingService;
    
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Integer id) {
        return contractRepository.findById(id);
    }

    @Transactional
    public Contract createContract(Contract contract) {
        if (contract.getSignDate() == null) {
            contract.setSignDate(new Date());
        }
        
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("active");
        }
        
        return contractRepository.save(contract);
    }
    
    @Transactional
    public Contract createContractWithInitialReading(Contract contract, WaterReading initialReading) {
        try {
            // First create and save the contract
            Contract savedContract = createContract(contract);
            
            // Then create and save the initial water reading
            if (initialReading != null) {
                initialReading.setApartment(contract.getApartment());
                
                if (initialReading.getReadingDate() == null) {
                    initialReading.setReadingDate(contract.getStartDate());
                }
                
                // Both previous and current readings should be set to the initial value
                // This ensures consumption will be 0 (no water used yet)
                
                waterReadingService.createWaterReading(initialReading);
            }
            
            return savedContract;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating contract with initial reading: " + e.getMessage(), e);
        }
    }
}