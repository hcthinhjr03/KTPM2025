package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.models.WaterServiceStatistics;
import com.example.ktpm_backend.services.WaterServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/water-service")
public class WaterServiceController {

    @Autowired
    private WaterServiceService waterServiceService;

    // Get all water services
    @GetMapping
    public ResponseEntity<List<WaterService>> getAllWaterServices() {
        List<WaterService> waterServices = waterServiceService.getAllWaterServices();
        return new ResponseEntity<>(waterServices, HttpStatus.OK);
    }

    // Get water service by id
    @GetMapping("/{id}")
    public ResponseEntity<WaterService> getWaterServiceById(@PathVariable Integer id) {
        Optional<WaterService> waterService = waterServiceService.getWaterServiceById(id);
        if (waterService.isPresent()) {
            return new ResponseEntity<>(waterService.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create new water service
    @PostMapping
    public ResponseEntity<WaterService> createWaterService(@RequestBody WaterService waterService) {
        WaterService createdWaterService = waterServiceService.createWaterService(waterService);
        return new ResponseEntity<>(createdWaterService, HttpStatus.CREATED);
    }

    // Update water service
    @PutMapping("/{id}")
    public ResponseEntity<WaterService> updateWaterService(
            @PathVariable Integer id,
            @RequestBody WaterService waterService) {
        Optional<WaterService> existingWaterService = waterServiceService.getWaterServiceById(id);
        if (existingWaterService.isPresent()) {
            waterService.setServiceId(id);
            WaterService updatedWaterService = waterServiceService.updateWaterService(waterService);
            return new ResponseEntity<>(updatedWaterService, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete water service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterService(@PathVariable Integer id) {
        Optional<WaterService> waterService = waterServiceService.getWaterServiceById(id);
        if (waterService.isPresent()) {
            waterServiceService.deleteWaterService(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get statistics for a specific water service
    @GetMapping("/statistics/{id}")
    public ResponseEntity<WaterServiceStatistics> getWaterServiceStatistics(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        
        Optional<WaterServiceStatistics> statistics = waterServiceService.getWaterServiceStatistics(id, fromDate, toDate);
        if (statistics.isPresent()) {
            return new ResponseEntity<>(statistics.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all water services with statistics
    @GetMapping("/statistics")
    public ResponseEntity<List<WaterServiceStatistics>> getAllWaterServiceStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        
        List<WaterServiceStatistics> statisticsList = waterServiceService.getAllWaterServiceStatistics(fromDate, toDate);
        return new ResponseEntity<>(statisticsList, HttpStatus.OK);
    }
}
