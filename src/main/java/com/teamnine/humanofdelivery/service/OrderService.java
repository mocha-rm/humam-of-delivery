package com.teamnine.humanofdelivery.service;

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


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    //TODO : UserRepository userRepository;


    @Transactional
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        Store findStore = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."));
        //가게가 영업 중인지, 최소 주문 금액을 만족하는지?
        if (!findStore.getStatus().equals(StoreStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "영업이 종료된 가게입니다.");
        }

        Long userId = 1L; //임시 값

        Order order = new Order(findStore, userId, orderRequestDto.getMenuName(),"ORDER_COMPLETED");
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }
}
