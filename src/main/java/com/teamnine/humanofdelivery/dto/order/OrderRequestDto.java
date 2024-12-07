package com.teamnine.humanofdelivery.dto.order;

import com.teamnine.humanofdelivery.status.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    private final Long storeId;
    private final String menuName;
    private final OrderStatus orderStatus;
}
