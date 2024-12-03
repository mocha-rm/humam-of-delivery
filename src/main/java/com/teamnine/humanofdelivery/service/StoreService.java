package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.dto.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.StoreResponseDto;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreResponseDto create(StoreRequestDto storeRequestDto) {
        Store createdStore = storeRequestDto.toEntity();
        storeRepository.save(createdStore);
        return new StoreResponseDto(createdStore);
    }

    public List<StoreResponseDto> findAll(String name) {
        return storeRepository.findAllByStoreName(name).stream().map(StoreResponseDto::toDto).toList();
    }
}