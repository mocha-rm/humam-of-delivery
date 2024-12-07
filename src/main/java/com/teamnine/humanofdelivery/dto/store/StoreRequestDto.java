package com.teamnine.humanofdelivery.dto.store;

import com.teamnine.humanofdelivery.status.StoreStatus;
import com.teamnine.humanofdelivery.entity.Member;
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

    public Store toEntity(Member member) {
        return Store.builder()
                .name(name)
                .member(member)
                .storeStatus(status)
                .minCost(minCost)
                .openAt(openAt)
                .closeAt(closeAt)
                .build();
    }
}
