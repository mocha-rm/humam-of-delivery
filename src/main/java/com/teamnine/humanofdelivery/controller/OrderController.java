package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.OrderRequestDto;
import com.teamnine.humanofdelivery.dto.OrderResponseDto;
import com.teamnine.humanofdelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.create(orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
