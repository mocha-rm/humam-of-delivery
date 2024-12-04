package com.teamnine.humanofdelivery.dto;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {
    private final Long id;
    private final Long storeId;
    private final Long userId;
    private final String menuName;
    private final OrderStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.storeId = order.getStore().getId();
        this.userId = order.getUserId();
        this.menuName = order.getMenuName();
        this.status = order.getOrderStatus();
        this.createdAt = order.getCreatedDate();
        this.modifiedAt = order.getModifiedDate();
    }

    public static OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getStore().getId(),
                order.getUserId(),
                order.getMenuName(),
                order.getOrderStatus(),
                order.getCreatedDate(),
                order.getModifiedDate()
        );
    }
}
