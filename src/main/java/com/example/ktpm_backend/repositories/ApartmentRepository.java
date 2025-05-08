package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.Customer;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    
    List<Apartment> findByCustomer(Customer customer);
    
}