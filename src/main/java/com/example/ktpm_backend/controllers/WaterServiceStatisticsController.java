package com.example.ktpm_backend.controllers;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterServiceStatistics;
import com.example.ktpm_backend.services.WaterServiceStatisticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistics/water-service")
public class WaterServiceStatisticsController {

    @Autowired
    private WaterServiceStatisticService waterServiceStatisticService;

    // thong ke toan bo
    @GetMapping
    public ResponseEntity<List<WaterServiceStatistics>> getAllWaterServiceStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        
        List<WaterServiceStatistics> statisticsList = waterServiceStatisticService.getAllWaterServiceStatistics(fromDate, toDate);
        return new ResponseEntity<>(statisticsList, HttpStatus.OK);
    }

    // thong ke chi tiet
    @GetMapping("/{id}")
    public ResponseEntity<WaterServiceStatistics> getWaterServiceStatistics(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        
        Optional<WaterServiceStatistics> statistics = waterServiceStatisticService.getWaterServiceStatistics(id, fromDate, toDate);
        if (statistics.isPresent()) {
            return new ResponseEntity<>(statistics.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // lay danh sach bill 
    @GetMapping("/{id}/bills")
    public ResponseEntity<List<Bill>> getWaterServiceBills(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        
        List<Bill> bills = waterServiceStatisticService.getWaterServiceBills(id, fromDate, toDate);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
}