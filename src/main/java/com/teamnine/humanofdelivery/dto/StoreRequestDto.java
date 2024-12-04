package com.teamnine.humanofdelivery.dto;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreRequestDto {
    private final String name;
    private final StoreStatus status;
    private final Integer minCost;
    private final LocalTime openAt;
    private final LocalTime closeAt;

    public Store toEntity() {
        return Store.builder()
                .name(name)
                .storeStatus(status)
                .minCost(minCost)
                .openAt(openAt)
                .closeAt(closeAt)
                .build();
    }
}
