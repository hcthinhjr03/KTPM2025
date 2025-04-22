package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.repositories.ApartmentRepository;
import com.example.ktpm_backend.repositories.ContractRepository;
import com.example.ktpm_backend.repositories.CustomerRepository;
import com.example.ktpm_backend.repositories.WaterServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ApartmentRepository apartmentRepository;
    
    @Autowired
    private WaterServiceRepository waterServiceRepository;

    // Lấy tất cả hợp đồng
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    // Lấy hợp đồng theo ID
    public Optional<Contract> getContractById(Integer id) {
        return contractRepository.findById(id);
    }

    // Tạo hợp đồng mới
    public Contract createContract(Contract contract) {
        // Nếu không có ngày ký, đặt ngày hiện tại
        if (contract.getSignDate() == null) {
            contract.setSignDate(new Date());
        }
        
        // Nếu không có trạng thái, đặt mặc định là 'active'
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("active");
        }
        
        return contractRepository.save(contract);
    }

    // Tạo hợp đồng với thông tin chi tiết
    public Optional<Contract> createContractWithDetails(Integer customerId, Integer apartmentId, 
                                                       Integer waterServiceId, Date startDate, 
                                                       Date endDate) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Apartment> apartmentOpt = apartmentRepository.findById(apartmentId);
        Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(waterServiceId);
        
        if (customerOpt.isPresent() && apartmentOpt.isPresent() && waterServiceOpt.isPresent()) {
            Contract contract = new Contract();
            contract.setCustomer(customerOpt.get());
            contract.setApartment(apartmentOpt.get());
            contract.setWaterService(waterServiceOpt.get());
            contract.setSignDate(new Date());
            contract.setStartDate(startDate);
            contract.setEndDate(endDate);
            contract.setStatus("active");
            
            return Optional.of(contractRepository.save(contract));
        }
        
        return Optional.empty();
    }

    // Cập nhật hợp đồng
    public Optional<Contract> updateContract(Integer id, Contract contractDetails) {
        Optional<Contract> contractOpt = contractRepository.findById(id);
        
        if (contractOpt.isPresent()) {
            Contract contract = contractOpt.get();
            
            // Cập nhật thông tin
            if (contractDetails.getStartDate() != null) {
                contract.setStartDate(contractDetails.getStartDate());
            }
            
            if (contractDetails.getEndDate() != null) {
                contract.setEndDate(contractDetails.getEndDate());
            }
            
            if (contractDetails.getStatus() != null) {
                contract.setStatus(contractDetails.getStatus());
            }
            
            // Cập nhật mối quan hệ nếu được cung cấp
            if (contractDetails.getCustomer() != null && contractDetails.getCustomer().getCustomerId() != null) {
                Optional<Customer> customerOpt = customerRepository.findById(contractDetails.getCustomer().getCustomerId());
                customerOpt.ifPresent(contract::setCustomer);
            }
            
            if (contractDetails.getApartment() != null && contractDetails.getApartment().getApartmentId() != null) {
                Optional<Apartment> apartmentOpt = apartmentRepository.findById(contractDetails.getApartment().getApartmentId());
                apartmentOpt.ifPresent(contract::setApartment);
            }
            
            if (contractDetails.getWaterService() != null && contractDetails.getWaterService().getServiceId() != null) {
                Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(contractDetails.getWaterService().getServiceId());
                waterServiceOpt.ifPresent(contract::setWaterService);
            }
            
            return Optional.of(contractRepository.save(contract));
        }
        
        return Optional.empty();
    }

    // Chấm dứt hợp đồng
    public Optional<Contract> terminateContract(Integer id) {
        Optional<Contract> contractOpt = contractRepository.findById(id);
        
        if (contractOpt.isPresent()) {
            Contract contract = contractOpt.get();
            contract.setStatus("terminated");
            contract.setEndDate(new Date());
            
            return Optional.of(contractRepository.save(contract));
        }
        
        return Optional.empty();
    }

    // Xóa hợp đồng
    public boolean deleteContract(Integer id) {
        if (contractRepository.existsById(id)) {
            contractRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Lấy hợp đồng theo khách hàng
    public List<Contract> getContractsByCustomer(Integer customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        
        if (customerOpt.isPresent()) {
            return contractRepository.findByCustomer(customerOpt.get());
        }
        
        return List.of();
    }

    // Lấy hợp đồng còn hiệu lực
    public List<Contract> getActiveContracts() {
        return contractRepository.findActiveContracts(new Date());
    }

    // Kiểm tra căn hộ đã có hợp đồng hoạt động chưa
    public boolean isApartmentHasActiveContract(Integer apartmentId) {
        Optional<Apartment> apartmentOpt = apartmentRepository.findById(apartmentId);
        
        if (apartmentOpt.isPresent()) {
            Apartment apartment = apartmentOpt.get();
            Date currentDate = new Date();
            
            // Kiểm tra có hợp đồng nào còn hiệu lực không
            if (apartment.getContracts() != null) {
                return apartment.getContracts().stream()
                    .anyMatch(contract -> "active".equals(contract.getStatus()) && 
                             (contract.getEndDate() == null || !contract.getEndDate().before(currentDate)));
            }
        }
        
        return false;
    }
}