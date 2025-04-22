package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    // Tìm khách hàng theo số CMND/CCCD
    Optional<Customer> findByIdentityCard(String identityCard);
    
    // Tìm khách hàng theo số điện thoại
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    // Tìm khách hàng theo email
    Optional<Customer> findByEmail(String email);
    
    // Tìm khách hàng theo tên
    List<Customer> findByFullNameContaining(String fullName);
}