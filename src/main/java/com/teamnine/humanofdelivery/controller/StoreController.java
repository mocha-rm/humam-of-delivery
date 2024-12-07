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
public class StoreController {
    private final StoreService storeService;

    /**
     * 메뉴 생성
     * @param storeRequestDto (스토어 생성에 필요한 정보 전달)
     * @return 생성된 스토어 정보
     */
    @PostMapping("/owner/stores")
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto storeRequestDto) {
        StoreResponseDto createdStore = storeService.create(storeRequestDto);
        return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
    }

    /**
     * 스토어 다건 조회
     * @param name (검색할 키워드를 받는 파라미터)
     * @return 이름이 포함된 스토어 목록
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> searchStores(@RequestParam String name) {
        List<StoreResponseDto> stores = storeService.findAll(name);
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    /**
     * 스토어 단건 조회
     * @param id (스토어 id)
     * @return 스토어 정보 및 메뉴목록
     */
    @GetMapping("/stores/{id}")
    public ResponseEntity<StoreWithMenusResponseDto> viewStore(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.findStore(id), HttpStatus.OK);
    }

    /**
     * 스토어 폐업
     * @param id (스토어 id)
     * @param storeRequestDto (스토어 폐업에 필요한 정보 전달)
     * @return 폐업된 스토어 정보
     */
    @PatchMapping("/owner/stores/{id}")
    public ResponseEntity<StoreResponseDto> storeShutdown(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto) {
        StoreResponseDto store = storeService.patchStoreStatus(id, storeRequestDto.getStatus());
        return new ResponseEntity<>(store, HttpStatus.OK);
    }
}
