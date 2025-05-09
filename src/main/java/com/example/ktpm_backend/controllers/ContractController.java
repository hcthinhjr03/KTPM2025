package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.models.WaterReading;
import com.example.ktpm_backend.services.ContractService;
import com.example.ktpm_backend.services.CustomerService;
import com.example.ktpm_backend.services.ApartmentService;
import com.example.ktpm_backend.services.WaterServiceService;
import com.example.ktpm_backend.services.WaterReadingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    
    @Autowired
    private WaterReadingService waterReadingService;

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Integer id) {
        Optional<Contract> contract = contractService.getContractById(id);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Transactional // Đảm bảo tính toàn vẹn giao dịch
    public ResponseEntity<?> createContract(@RequestBody ContractRequest contractRequest) {
        try {
            Contract contract = contractRequest.getContract();
            Double initialReading = contractRequest.getInitialReading();
            
            // Validate
            if (contract.getCustomer() == null || contract.getCustomer().getCustomerId() == null) {
                return new ResponseEntity<>("Customer information is required", HttpStatus.BAD_REQUEST);
            }
            if (contract.getApartment() == null || contract.getApartment().getApartmentId() == null) {
                return new ResponseEntity<>("Apartment information is required", HttpStatus.BAD_REQUEST);
            }
            if (contract.getWaterService() == null || contract.getWaterService().getServiceId() == null) {
                return new ResponseEntity<>("Water service information is required", HttpStatus.BAD_REQUEST);
            }
            if (initialReading == null) {
                return new ResponseEntity<>("Initial water reading is required", HttpStatus.BAD_REQUEST);
            }

            // Load entities
            Optional<Customer> customerOpt = customerService.getCustomerById(contract.getCustomer().getCustomerId());
            Optional<Apartment> apartmentOpt = apartmentService.getApartmentById(contract.getApartment().getApartmentId());
            Optional<WaterService> waterServiceOpt = waterServiceService.getWaterServiceById(contract.getWaterService().getServiceId());
            
            if (!customerOpt.isPresent() || !apartmentOpt.isPresent() || !waterServiceOpt.isPresent()) {
                return new ResponseEntity<>("One or more required entities not found", HttpStatus.BAD_REQUEST);
            }
        
            contract.setCustomer(customerOpt.get());
            contract.setApartment(apartmentOpt.get());
            contract.setWaterService(waterServiceOpt.get());
            
            if (contract.getSignDate() == null) {
                contract.setSignDate(new Date());
            }

            if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
                contract.setStatus("active");
            }
            
            // Tạo Contract trước
            Contract newContract = contractService.createContract(contract);
            
            // Sau đó tạo WaterReading và lưu trực tiếp
            WaterReading waterReading = new WaterReading();
            waterReading.setApartment(apartmentOpt.get());
            waterReading.setReadingDate(contract.getStartDate());
            
            // Set both readings to the same initial value
            waterReading.setPreviousReading(initialReading);
            waterReading.setCurrentReading(initialReading);
            
            // Lưu trực tiếp WaterReading
            WaterReading savedReading = waterReadingService.createWaterReading(waterReading);
            
            if (savedReading == null || savedReading.getReadingId() == null) {
                throw new RuntimeException("Failed to save water reading");
            }
            
            System.out.println("Successfully saved water reading with ID: " + savedReading.getReadingId());
            
            return new ResponseEntity<>(newContract, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating contract: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

// Simple DTO class for the request
class ContractRequest {
    private Contract contract;
    private Double initialReading;
    
    public Contract getContract() {
        return contract;
    }
    
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    
    public Double getInitialReading() {
        return initialReading;
    }
    
    public void setInitialReading(Double initialReading) {
        this.initialReading = initialReading;
    }
}