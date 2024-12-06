package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.status.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    /**
     * 이름으로 폐업상태가 아닌 스토어 모두 반환
     * @param name (이름)
     * @param storeStatus (스토어 상태, OPEN, SHUT)
     * @return 스토어 리스트
     */
    @Query("select s from Store s where s.name like %:name% and s.status <> :status")
    List<Store> findAllByStoreName(@Param("name") String name, @Param("status") StoreStatus storeStatus);

    /**
     * 유저가 가지고 있는 오픈된 스토어의 갯수 반환
     * @param storeStatus (스토어 상태, OPEN, SHUT)
     * @param userId (유저 id)
     * @return 유저가 가지고 있는 오픈된 스토어의 갯수
     */
    @Query("select count(s) from Store s where s.status = :status and s.member.userId = :userId")
    int findOpenStore(@Param("status") StoreStatus storeStatus, @Param("userId") Long userId);

    /**
     * userId와 storeId가 일치하는 스토어 반환
     * @param memberId (유저 id)
     * @param storeId (스토어 id)
     * @return userId와 storeId가 일치하는 스토어
     */
    @Query("SELECT s FROM Store s WHERE s.member.userId = :memberId AND s.id = :storeId")
    Optional<Store> findByMember_IdAndId(@Param("memberId") Long memberId, @Param("storeId") Long storeId);

    /**
     * 유저가 소유한 스토어 반환
     * @param ownerId (유저 id)
     * @return 소유한 스토어 리스트
     */
    @Query("select s from Store s where s.member.userId = :ownerId")
    List<Store> findAllByOwnerId(@Param("ownerId") Long ownerId);

    /**
     * 폐업상태가 아닌 스토어 조회
     * @param ownerId (유저 id)
     * @return 유저가 소유한 스토어의 카운트
     */
    @Query("SELECT COUNT(s) FROM Store s WHERE s.member.userId = :ownerId AND s.status <> 'SHUT'")
    long countActiveStoresByOwnerId(@Param("ownerId") Long ownerId);
}

