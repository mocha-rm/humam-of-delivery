package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Order 테이블의 id와 가게의 fk인 user_id가 일치하는 주문의 갯수
     * @param ownerId (유저 id)
     * @param orderId (주문 id)
     * @return Order 테이블의 id와 가게의 fk인 user_id가 일치하면 true 아니면 false
     */
    @Query("select count(o) > 0 from Order o where o.store.member.userId = :ownerId and o.id = :orderId")
    boolean existsByOwnerAndOrder(@Param("ownerId") Long ownerId, @Param("orderId") Long orderId);
}
