package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.dto.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.StoreResponseDto;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto create(StoreRequestDto storeRequestDto) {
        Store createdStore = storeRequestDto.toEntity();
        storeRepository.save(createdStore);
        return new StoreResponseDto(createdStore);
    }

    public List<StoreResponseDto> findAll(String name) {
        return storeRepository.findAllByStoreName(name, StoreStatus.SHUT).stream().map(StoreResponseDto::toDto).toList();
    }

    @Transactional
    public StoreResponseDto patchStoreStatus(Long id, StoreStatus storeStatus) {
        Store findStore = getStore(id);
        if (storeStatus.equals(findStore.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 " + storeStatus + "된 상태입니다.");
        }
        findStore.patchStatus(storeStatus);
        storeRepository.save(findStore);
        return new StoreResponseDto(findStore);
    }

    private Store getStore(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}