package com.teamnine.humanofdelivery.dto.user;

import com.teamnine.humanofdelivery.status.StoreStatus;
import com.teamnine.humanofdelivery.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OwnerResponseDto {

    private final String name;

    private final String email;

    private final UserRole role;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final Long storeNum;

    private final List<StoreDetail> stores;

    @Getter
    @RequiredArgsConstructor
    public static class StoreDetail {
        private final Long storeId;
        private final String storeName;
        private final StoreStatus storeStatus;
    }
}
