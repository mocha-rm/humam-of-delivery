package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.status.OrderStatus;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.order.OrderRequestDto;
import com.teamnine.humanofdelivery.dto.order.OrderResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Menu;
import com.teamnine.humanofdelivery.entity.Order;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.enums.UserRole;
import com.teamnine.humanofdelivery.exception.order.OrderErrorCode;
import com.teamnine.humanofdelivery.exception.order.OrderException;
import com.teamnine.humanofdelivery.exception.store.StoreErrorCode;
import com.teamnine.humanofdelivery.exception.store.StoreException;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.MenuRepository;
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
    private final MenuRepository menuRepository;
    private final SessionUtils sessionUtils;


    /**
     * 주문 생성
     * @param orderRequestDto 주문 생성에 필요한 정보(storeId, menuName, orderStatus)
     * @return 생성된 주문 정보(id, storeId, userId, menuName, status, createdAt, modifiedAt)
     */
    @Transactional
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        sessionUtils.checkAuthorization(findMember);

        Store findStore = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() ->
                new StoreException(StoreErrorCode.STORE_NOT_FOUND)
        );
        if (LocalTime.now().isBefore(findStore.getOpenAt()) || LocalTime.now().isAfter(findStore.getCloseAt())) {
            throw new OrderException(OrderErrorCode.ORDER_ERROR_USER_01);
        }

        List<Menu> menus = menuRepository.findActiveMenusByStoreId(findStore.getId());
        Menu findMenu = menus.stream().filter(menu -> menu.getMenuName().equals(orderRequestDto.getMenuName())).findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴는 존재하지 않습니다.")
        );

        if (findMenu.getPrice() < findStore.getMinCost()) {
            throw new OrderException(OrderErrorCode.ORDER_ERROR_USER_02);
        }

        Order order = new Order(findStore, findMember, orderRequestDto.getMenuName(), OrderStatus.ORDER_COMPLETED);
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    /**
     * 주문 상태 변경
     * @param id (주문 id)
     * @param orderRequestDto 주문 상태 변경에 필요한 정보(orderStatus)
     * @return 수정된 주문 정보(id, storeId, userId, menuName, status, createdAt, modifiedAt)
     */
    @Transactional
    public OrderResponseDto patchOrderStatus(Long id, OrderRequestDto orderRequestDto) {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        sessionUtils.checkAuthorization(findMember);

        Order findOrder = orderRepository.findById(id).orElseThrow(() ->
                new OrderException(OrderErrorCode.ORDER_NOT_FOUND)
        );

        boolean isMyOrder = orderRepository.existsByOwnerAndOrder(findMember.getUserId(), findOrder.getId());
        if (!isMyOrder) {
            throw new OrderException(OrderErrorCode.ORDER_ERROR_AUTHORIZATION_03);
        }

        if (orderRequestDto.getOrderStatus().equals(findOrder.getOrderStatus())) {
            throw new OrderException(OrderErrorCode.ORDER_ERROR_OWNER_01);
        }

        findOrder.patchStatus(orderRequestDto.getOrderStatus());
        orderRepository.save(findOrder);
        return new OrderResponseDto(findOrder);
    }

    /**
     * 주문 내역 조회
     * @return 주문이 완료된 목록
     */
    public List<OrderResponseDto> findAll() {
        return orderRepository.findAll().stream().map(OrderResponseDto::toDto).toList();
    }
}
