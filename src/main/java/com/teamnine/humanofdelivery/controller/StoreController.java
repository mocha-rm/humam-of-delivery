package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.store.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.store.StoreResponseDto;
import com.teamnine.humanofdelivery.dto.store.StoreWithMenusResponseDto;
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
    public ResponseEntity<List<StoreResponseDto>> searchStores(@RequestParam String name) {
        List<StoreResponseDto> stores = storeService.findAll(name);
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreWithMenusResponseDto> viewStore(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.findStore(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> storeShutdown(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto) {
        StoreResponseDto store = storeService.patchStoreStatus(id, storeRequestDto.getStatus());
        return new ResponseEntity<>(store, HttpStatus.OK);
    }
}
