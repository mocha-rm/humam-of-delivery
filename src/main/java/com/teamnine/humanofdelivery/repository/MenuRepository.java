package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.MenuStatus;
import com.teamnine.humanofdelivery.entity.base.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 가게의 모든 메뉴 조회 (상태 필터링)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :storeId AND m.menuStatus = 'ACTIVE'")
    List<Menu> findActiveMenusByStoreId(@Param("storeId") Long restaurantId);
}

