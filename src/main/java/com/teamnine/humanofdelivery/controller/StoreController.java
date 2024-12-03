package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.StoreResponseDto;
import com.teamnine.humanofdelivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto storeRequestDto) {
        StoreResponseDto createdStore = storeService.create(storeRequestDto);
        return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> findAllStore(@RequestParam String name) {
        List<StoreResponseDto> stores = storeService.findAll(name);
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }
}
