package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store s where s.name like %:name% and s.status <> :status")
    List<Store> findAllByStoreName(@Param("name") String name, @Param("status")StoreStatus storeStatus);

    @Query("select count(s) from Store s where s.status = :status and s.member.userId = :userId")
    int findOpenStore(@Param("status") StoreStatus storeStatus, @Param("userId") Long userId);

    Optional<Store> findByMember_IdAndStore_Id(Long memberId, Long storeId);

    // 사장의 store 조회
    @Query("select s from Store s Where s.owner.id = :ownerId")
    List<Store> findAllByOwnerId(@Param("ownerId") Long ownerId);

    // SHUT DOWN 상태가 아닌 가게 조회
    @Query("SELECT COUNT(s) FROM Store s WHERE s.owner.id = :ownerId AND s.status <> 'SHUTDOWN'")
    long countActiveStoresByOwnerId(@Param("ownerId") Long ownerId);
}

