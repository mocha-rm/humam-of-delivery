package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.exception.user.UserErrorCode;
import com.teamnine.humanofdelivery.exception.user.UserException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * 회원 정보를 관리하기 위한 JPA Repository 인터페이스입니다.
 * Member 엔티티와 관련된 데이터 접근 로직을 제공합니다.
 * @author 이빛나
 */
@Repository
public interface MemberRepository extends JpaRepository <Member, Long> {

    /**
     * 이름으로 회원을 검색하고, 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param name 검색할 회원의 이름
     * @return 해당 이름을 가진 회원 엔티티
     * @throws UserException 해당 이름의 회원이 없을 경우 예외를 발생
     */
    Optional<Member> findByName(String name);
    default Member findByNameOrElseThrow(String name) {
        return findByName(name).orElseThrow(()-> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * ID로 회원을 검색하고, 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param id 검색할 회원의 ID
     * @return 해당 ID를 가진 회원 엔티티
     * @throws UserException 해당 ID의 회원이 없을 경우 예외를 발생
     */
    default Member findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 이메일로 회원을 검색하고, 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param email 검색할 회원의 이메일
     * @return 해당 이메일을 가진 회원 엔티티
     * @throws UserException 해당 이메일의 회원이 없을 경우 예외를 발생
     */
    Optional<Member> findByEmail(String email);
    default Member findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() ->  new UserException(UserErrorCode.EMAIL_NOT_FOUND));
    }

    /**
     * 이메일 중복여부를 검사합니다.
     *
     * @param email 검색할 회원의 이메일
     * @return 해당 이메일을 가진 회원 엔티티
     */
    boolean existsByEmail(String email);
}
