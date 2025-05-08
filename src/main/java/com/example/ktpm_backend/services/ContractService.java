package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.repositories.ContractRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;
    
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Integer id) {
        return contractRepository.findById(id);
    }

    public Contract createContract(Contract contract) {
        if (contract.getSignDate() == null) {
            contract.setSignDate(new Date());
        }
        
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("active");
        }
        
        return contractRepository.save(contract);
    }

}