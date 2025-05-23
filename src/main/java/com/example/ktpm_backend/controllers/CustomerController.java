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

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/identity-card")
    public ResponseEntity<Customer> findByIdentityCard(@RequestParam String identityCard) {
        Optional<Customer> customer = customerService.findByIdentityCard(identityCard);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/phone")
    public ResponseEntity<Customer> findByPhoneNumber(@RequestParam String phoneNumber) {
        Optional<Customer> customer = customerService.findByPhoneNumber(phoneNumber);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/email")
    public ResponseEntity<Customer> findByEmail(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Customer>> searchByName(@RequestParam String fullName) {
        List<Customer> customers = customerService.searchByName(fullName);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}