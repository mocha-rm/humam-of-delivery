package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select count(o) > 0 from Order o where o.store.member.userId = :ownerId and o.id = :orderId")
    boolean existsByOwnerAndOrder(@Param("ownerId") Long ownerId, @Param("orderId") Long orderId);
}
