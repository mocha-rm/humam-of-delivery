package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //List<Review> findAllByStore_StoreIdOrderByCreatedAtDesc(Long storeId);

    //List<Review> findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(Long storeId, int mintRate, int maxRate);

    //boolean existsByOrder_OrderId(Long orderId);

    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId ORDER BY r.createdDate DESC")
    List<Review> findAllByStore_StoreIdOrderByCreatedAtDesc(@Param("storeId") Long storeId);

    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.rate BETWEEN :minRate AND :maxRate ORDER BY r.createdDate DESC")
    List<Review> findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(@Param("storeId") Long storeId, @Param("minRate") int minRate, @Param("maxRate") int maxRate);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.id = :orderId")
    boolean existsByOrder_OrderId(@Param("orderId") Long orderId);
}
