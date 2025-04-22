package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Customer;
import com.example.ktpm_backend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Lấy khách hàng theo ID
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    // Tìm khách hàng theo CMND/CCCD
    public Optional<Customer> findByIdentityCard(String identityCard) {
        return customerRepository.findByIdentityCard(identityCard);
    }

    // Tìm khách hàng theo số điện thoại
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    // Tìm khách hàng theo email
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // Tìm khách hàng theo tên
    public List<Customer> searchByName(String fullName) {
        return customerRepository.findByFullNameContaining(fullName);
    }

    // Tạo khách hàng mới
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Cập nhật khách hàng
    public Optional<Customer> updateCustomer(Integer id, Customer customerDetails) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            if (customerDetails.getFullName() != null) {
                customer.setFullName(customerDetails.getFullName());
            }
            
            if (customerDetails.getIdentityCard() != null) {
                customer.setIdentityCard(customerDetails.getIdentityCard());
            }
            
            if (customerDetails.getPhoneNumber() != null) {
                customer.setPhoneNumber(customerDetails.getPhoneNumber());
            }
            
            if (customerDetails.getEmail() != null) {
                customer.setEmail(customerDetails.getEmail());
            }
            
            if (customerDetails.getAddress() != null) {
                customer.setAddress(customerDetails.getAddress());
            }
            
            return Optional.of(customerRepository.save(customer));
        }
        
        return Optional.empty();
    }

    // Xóa khách hàng
    public boolean deleteCustomer(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}