package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStore_StoreIdOrderByCreatedAtDesc(Long storeId);

    List<Review> findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(Long storeId, int mintRate, int maxRate);

    boolean existsByOrder_OrderId(Long orderId);
}
