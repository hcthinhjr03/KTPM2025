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
    

    // Lấy tất cả hợp đồng
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    // Lấy hợp đồng theo ID
    public Optional<Contract> getContractById(Integer id) {
        return contractRepository.findById(id);
    }

    // Tạo hợp đồng mới
    public Contract createContract(Contract contract) {
        // Nếu không có ngày ký, đặt ngày hiện tại
        if (contract.getSignDate() == null) {
            contract.setSignDate(new Date());
        }
        
        // Nếu không có trạng thái, đặt mặc định là 'active'
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("active");
        }
        
        return contractRepository.save(contract);
    }

}