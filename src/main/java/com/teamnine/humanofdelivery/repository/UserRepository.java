package com.teamnine.humanofdelivery.repository;

import com.teamnine.humanofdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    // 이름 존재 유무 확인
    Optional<User> findByName(String name);
    default User findByNameOrElseThrow(String name) {
        return findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이름입니다" + name));
    }

    // UserId 존재 유무 확인
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다." + id));
    }

    // DB email(로그인 시 사용되는 id) 존재여부 확인
    Optional<User> findByEmail(String email);
    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다." + email));
    }
}
