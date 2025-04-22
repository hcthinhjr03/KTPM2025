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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    
    // Tạo hợp đồng từ thông tin cơ bản
    @PostMapping("/create-simple")
    public ResponseEntity<?> createSimpleContract(
            @RequestParam Integer customerId,
            @RequestParam Integer apartmentId,
            @RequestParam Integer waterServiceId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        Optional<Contract> contract = contractService.createContractWithDetails(
                customerId, apartmentId, waterServiceId, 
                startDate != null ? startDate : new Date(), endDate);
        
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // Tạo hợp đồng với thông tin chi tiết
    @PostMapping("/create-with-details")
    public ResponseEntity<Contract> createContractWithDetails(
            @RequestParam Integer customerId,
            @RequestParam Integer apartmentId,
            @RequestParam Integer waterServiceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        Optional<Contract> contract = contractService.createContractWithDetails(
                customerId, apartmentId, waterServiceId, startDate, endDate);
        
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // Cập nhật hợp đồng
    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Integer id,
            @RequestBody Contract contractDetails) {
        
        Optional<Contract> updatedContract = contractService.updateContract(id, contractDetails);
        
        return updatedContract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Chấm dứt hợp đồng
    @PutMapping("/{id}/terminate")
    public ResponseEntity<Contract> terminateContract(@PathVariable Integer id) {
        Optional<Contract> terminatedContract = contractService.terminateContract(id);
        
        return terminatedContract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Xóa hợp đồng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Integer id) {
        boolean deleted = contractService.deleteContract(id);
        
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Lấy hợp đồng theo khách hàng
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Contract>> getContractsByCustomer(@PathVariable Integer customerId) {
        List<Contract> contracts = contractService.getContractsByCustomer(customerId);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    // Lấy hợp đồng còn hiệu lực
    @GetMapping("/active")
    public ResponseEntity<List<Contract>> getActiveContracts() {
        List<Contract> contracts = contractService.getActiveContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    // Kiểm tra căn hộ đã có hợp đồng hoạt động chưa
    @GetMapping("/check-apartment/{apartmentId}")
    public ResponseEntity<Map<String, Boolean>> checkApartmentContract(@PathVariable Integer apartmentId) {
        boolean hasActiveContract = contractService.isApartmentHasActiveContract(apartmentId);
        return new ResponseEntity<>(Map.of("hasActiveContract", hasActiveContract), HttpStatus.OK);
    }
}