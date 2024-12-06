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

    /**
     * 주문 생성
     * @param orderRequestDto (주문 생성에 필요한 정보 전달)
     * @return 주문 정보
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.create(orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * 주문 상태 변경
     * @param id (주문 id)
     * @param orderRequestDto (주문 상태)
     * @return 수정된 주문 정보
     */
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> patchOrderStatus(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.patchOrderStatus(id, orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 주문 조회
     * @return 주문이 완료된 목록
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> viewOrders() {
        List<OrderResponseDto> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
