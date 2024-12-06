package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.order.OrderRequestDto;
import com.teamnine.humanofdelivery.dto.order.OrderResponseDto;
import com.teamnine.humanofdelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> patchOrderStatus(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.patchOrderStatus(id, orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //TODO : 일반사용자, 가게소유자 별로 필터링 가능하도록 로직 수정하기
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> viewOrders() {
        List<OrderResponseDto> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
