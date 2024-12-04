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
        //TODO : 권한 확인 로직, 폐업 상태가 아닌 가게를 최대 3개까지만 생성가능
        StoreResponseDto createdStore = storeService.create(storeRequestDto);
        return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> searchStores(@RequestParam String name) {
        List<StoreResponseDto> stores = storeService.findAll(name);
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    //TODO : 특정 가게 조회 -> 메뉴 목록도 같이 보여질 수 있도록 구현하기
    @GetMapping("/{id}")
    public ResponseEntity<?> viewStore(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> storeShutdown(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto) {
        StoreResponseDto store = storeService.patchStoreStatus(id, storeRequestDto.getStatus());
        return new ResponseEntity<>(store, HttpStatus.OK);
    }
}
