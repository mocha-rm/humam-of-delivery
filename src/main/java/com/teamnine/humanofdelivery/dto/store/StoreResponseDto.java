package com.teamnine.humanofdelivery.dto.store;

import com.teamnine.humanofdelivery.status.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreResponseDto {
    private final Long id;
    private final String name;
    private final StoreStatus status;
    private final Integer minCost;
    private final LocalTime openAt;
    private final LocalTime closeAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.status = store.getStatus();
        this.minCost = store.getMinCost();
        this.openAt = store.getOpenAt();
        this.closeAt = store.getCloseAt();
        this.createdAt = store.getCreatedDate();
        this.modifiedAt = store.getModifiedDate();
    }

    public static StoreResponseDto toDto(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getStatus(),
                store.getMinCost(),
                store.getOpenAt(),
                store.getCloseAt(),
                store.getCreatedDate(),
                store.getModifiedDate());
    }
}
