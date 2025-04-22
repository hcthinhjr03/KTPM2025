package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.models.WaterService;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    
    // Tìm hợp đồng theo khách hàng
    List<Contract> findByCustomer(Customer customer);
    
    // Tìm hợp đồng theo dịch vụ nước
    List<Contract> findByWaterService(WaterService waterService);
    
    // Tìm hợp đồng còn hiệu lực
    @Query("SELECT c FROM Contract c WHERE c.status = 'active' AND (c.endDate IS NULL OR c.endDate >= :currentDate)")
    List<Contract> findActiveContracts(@Param("currentDate") Date currentDate);
    
    // Tìm hợp đồng theo khách hàng và trạng thái
    List<Contract> findByCustomerAndStatus(Customer customer, String status);
}