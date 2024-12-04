package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.dto.OrderRequestDto;
import com.teamnine.humanofdelivery.dto.OrderResponseDto;
import com.teamnine.humanofdelivery.entity.Order;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.repository.OrderRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    //TODO : UserRepository userRepository;


    @Transactional
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        Store findStore = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."));
        //TODO : 가게가 영업 중인지, 최소 주문 금액을 만족하는지?
        if (!findStore.getStatus().equals(StoreStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "영업이 종료된 가게입니다.");
        }

        Long userId = 1L; //임시 값

        Order order = new Order(findStore, userId, orderRequestDto.getMenuName(), OrderStatus.ORDER_COMPLETED);
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto patchOrderStatus(Long id, OrderRequestDto orderRequestDto) {
        Order findOrder = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."));
        //TODO : 권한확인 필요 (가게 소유자만 가능, 본인 가게만 가능)
        if (orderRequestDto.getOrderStatus().equals(findOrder.getOrderStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 상태로는 변경할 수 없습니다.");
        }

        findOrder.patchStatus(orderRequestDto.getOrderStatus());
        orderRepository.save(findOrder);
        return new OrderResponseDto(findOrder);
    }

    public List<OrderResponseDto> findAll() {
        return orderRepository.findAll().stream().map(OrderResponseDto::toDto).toList();
    }
}
