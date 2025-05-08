package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.repositories.ApartmentRepository;
import com.example.ktpm_backend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Optional<Apartment> getApartmentById(Integer id) {
        return apartmentRepository.findById(id);
    }

    public List<Apartment> getApartmentsByCustomer(Integer customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        
        if (customerOpt.isPresent()) {
            return apartmentRepository.findByCustomer(customerOpt.get());
        }
        
        return List.of();
    }


}