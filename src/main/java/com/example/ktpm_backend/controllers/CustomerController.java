package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Lấy tất cả khách hàng
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // Lấy khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tìm khách hàng theo CMND/CCCD
    @GetMapping("/search/identity-card")
    public ResponseEntity<Customer> findByIdentityCard(@RequestParam String identityCard) {
        Optional<Customer> customer = customerService.findByIdentityCard(identityCard);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tìm khách hàng theo số điện thoại
    @GetMapping("/search/phone")
    public ResponseEntity<Customer> findByPhoneNumber(@RequestParam String phoneNumber) {
        Optional<Customer> customer = customerService.findByPhoneNumber(phoneNumber);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tìm khách hàng theo email
    @GetMapping("/search/email")
    public ResponseEntity<Customer> findByEmail(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tìm khách hàng theo tên
    @GetMapping("/search/name")
    public ResponseEntity<List<Customer>> searchByName(@RequestParam String fullName) {
        List<Customer> customers = customerService.searchByName(fullName);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // Tạo khách hàng mới
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    // Cập nhật khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Integer id,
            @RequestBody Customer customerDetails) {
        
        Optional<Customer> updatedCustomer = customerService.updateCustomer(id, customerDetails);
        
        return updatedCustomer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Xóa khách hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        boolean deleted = customerService.deleteCustomer(id);
        
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}