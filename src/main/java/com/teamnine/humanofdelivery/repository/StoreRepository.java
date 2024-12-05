package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store s where s.name like %:name% and s.status <> :status")
    List<Store> findAllByStoreName(@Param("name") String name, @Param("status")StoreStatus storeStatus);
    Store findByUser_IdAndStore_Id(Long userId, Long storeId);
}
