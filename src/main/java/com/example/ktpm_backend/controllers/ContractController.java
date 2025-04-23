package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.services.ContractService;
import com.example.ktpm_backend.services.CustomerService;
import com.example.ktpm_backend.services.ApartmentService;
import com.example.ktpm_backend.services.WaterServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ApartmentService apartmentService;
    
    @Autowired
    private WaterServiceService waterServiceService;

    // Lấy tất cả hợp đồng
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    // Lấy hợp đồng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Integer id) {
        Optional<Contract> contract = contractService.getContractById(id);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tạo hợp đồng mới
    @PostMapping
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        // Kiểm tra thông tin liên quan
        if (contract.getCustomer() == null || contract.getCustomer().getCustomerId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        if (contract.getApartment() == null || contract.getApartment().getApartmentId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        if (contract.getWaterService() == null || contract.getWaterService().getServiceId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Lấy đối tượng đầy đủ từ database
        Optional<Customer> customerOpt = customerService.getCustomerById(contract.getCustomer().getCustomerId());
        Optional<Apartment> apartmentOpt = apartmentService.getApartmentById(contract.getApartment().getApartmentId());
        Optional<WaterService> waterServiceOpt = waterServiceService.getWaterServiceById(contract.getWaterService().getServiceId());
        
        if (!customerOpt.isPresent() || !apartmentOpt.isPresent() || !waterServiceOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Thiết lập thông tin đầy đủ
        contract.setCustomer(customerOpt.get());
        contract.setApartment(apartmentOpt.get());
        contract.setWaterService(waterServiceOpt.get());
        
        // Thiết lập ngày ký nếu chưa có
        if (contract.getSignDate() == null) {
            contract.setSignDate(new Date());
        }
        
        // Thiết lập trạng thái nếu chưa có
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("active");
        }
        
        Contract newContract = contractService.createContract(contract);
        return new ResponseEntity<>(newContract, HttpStatus.CREATED);
    }
    
}