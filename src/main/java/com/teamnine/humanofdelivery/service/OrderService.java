package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.OrderRequestDto;
import com.teamnine.humanofdelivery.dto.OrderResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Order;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.enums.UserRole;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.OrderRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final SessionUtils sessionUtils;


    @Transactional
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        if (findMember.getRole().equals(UserRole.OWNER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "주문은 일반 사용자 계정을 이용해주세요.");
        }

        Store findStore = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."));
        if (LocalTime.now().isBefore(findStore.getOpenAt()) || LocalTime.now().isAfter(findStore.getCloseAt())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "영업시간이 아닙니다.");
        }
        //TODO : 최소 주문 금액을 만족하는지?
        //TODO : 해당 메뉴가 존재 하는지?

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
