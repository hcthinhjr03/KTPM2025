package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.Customer;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    
    // Tìm căn hộ theo khách hàng
    List<Apartment> findByCustomer(Customer customer);
    
    // Tìm căn hộ theo trạng thái
    List<Apartment> findByStatus(String status);
    
    // Tìm căn hộ theo tòa nhà
    List<Apartment> findByBuilding(String building);
}