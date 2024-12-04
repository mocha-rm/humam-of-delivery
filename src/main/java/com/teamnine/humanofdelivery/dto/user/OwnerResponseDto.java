package com.teamnine.humanofdelivery.dto.user;

import com.teamnine.humanofdelivery.entity.User;
import com.teamnine.humanofdelivery.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OwnerResponseDto {

    private final String name;

    private final String email;

    private final UserRole role;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final Long numRestaurants;

    private final String storeId;

    private final String storeName;

    private final String storeStatus;

    public static OwnerResponseDto toDto(User user) {
        return new OwnerResponseDto(
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedDate(),
                user.getModifiedDate(),
                user.getNumRestaurants(),
                null,
                null,
                null
        );
    }
//    public static OwnerResponseDto toDto(User user, Store store) {
//        return new OwnerResponseDto(
//                user.getName(),
//                user.getEmail(),
//                user.getRole(),
//                user.getCreatedDate(),
//                user.getModifiedDate(),
//                user.getNumRestaurants(),
//                store.getStoreId(),
//                store.getStoreName(),
//                store.getStoreStatus()
//        );
//    }
}
