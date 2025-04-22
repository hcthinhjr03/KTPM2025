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

    // Lấy tất cả căn hộ
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    // Lấy căn hộ theo ID
    public Optional<Apartment> getApartmentById(Integer id) {
        return apartmentRepository.findById(id);
    }

    // Lấy căn hộ theo khách hàng
    public List<Apartment> getApartmentsByCustomer(Integer customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        
        if (customerOpt.isPresent()) {
            return apartmentRepository.findByCustomer(customerOpt.get());
        }
        
        return List.of();
    }

    // Lấy căn hộ theo trạng thái
    public List<Apartment> getApartmentsByStatus(String status) {
        return apartmentRepository.findByStatus(status);
    }

    // Lấy căn hộ theo tòa nhà
    public List<Apartment> getApartmentsByBuilding(String building) {
        return apartmentRepository.findByBuilding(building);
    }

    // Tạo căn hộ mới
    public Apartment createApartment(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    // Cập nhật căn hộ
    public Optional<Apartment> updateApartment(Integer id, Apartment apartmentDetails) {
        Optional<Apartment> apartmentOpt = apartmentRepository.findById(id);
        
        if (apartmentOpt.isPresent()) {
            Apartment apartment = apartmentOpt.get();
            
            if (apartmentDetails.getApartmentNumber() != null) {
                apartment.setApartmentNumber(apartmentDetails.getApartmentNumber());
            }
            
            if (apartmentDetails.getBuilding() != null) {
                apartment.setBuilding(apartmentDetails.getBuilding());
            }
            
            if (apartmentDetails.getArea() != null) {
                apartment.setArea(apartmentDetails.getArea());
            }
            
            if (apartmentDetails.getStatus() != null) {
                apartment.setStatus(apartmentDetails.getStatus());
            }
            
            if (apartmentDetails.getCustomer() != null && apartmentDetails.getCustomer().getCustomerId() != null) {
                Optional<Customer> customerOpt = customerRepository.findById(apartmentDetails.getCustomer().getCustomerId());
                customerOpt.ifPresent(apartment::setCustomer);
            }
            
            return Optional.of(apartmentRepository.save(apartment));
        }
        
        return Optional.empty();
    }

    // Xóa căn hộ
    public boolean deleteApartment(Integer id) {
        if (apartmentRepository.existsById(id)) {
            apartmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}