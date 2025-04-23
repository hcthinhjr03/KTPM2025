package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    // Lấy tất cả căn hộ
    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    // Lấy căn hộ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable Integer id) {
        Optional<Apartment> apartment = apartmentService.getApartmentById(id);
        return apartment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Lấy căn hộ theo khách hàng
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Apartment>> getApartmentsByCustomer(@PathVariable Integer customerId) {
        List<Apartment> apartments = apartmentService.getApartmentsByCustomer(customerId);
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    // Tạo căn hộ mới
    @PostMapping
    public ResponseEntity<Apartment> createApartment(@RequestBody Apartment apartment) {
        Apartment newApartment = apartmentService.createApartment(apartment);
        return new ResponseEntity<>(newApartment, HttpStatus.CREATED);
    }

    // Cập nhật căn hộ
    @PutMapping("/{id}")
    public ResponseEntity<Apartment> updateApartment(
            @PathVariable Integer id,
            @RequestBody Apartment apartmentDetails) {
        
        Optional<Apartment> updatedApartment = apartmentService.updateApartment(id, apartmentDetails);
        
        return updatedApartment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Xóa căn hộ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Integer id) {
        boolean deleted = apartmentService.deleteApartment(id);
        
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}