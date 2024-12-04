package com.teamnine.humanofdelivery.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    private final Long storeId;
    private final String menuName;
}
