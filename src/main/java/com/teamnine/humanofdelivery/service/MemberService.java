package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.config.Password.PasswordEncoder;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.user.LoginRequestDto;
import com.teamnine.humanofdelivery.dto.user.OwnerResponseDto;
import com.teamnine.humanofdelivery.dto.user.UserResponseDto;
import com.teamnine.humanofdelivery.dto.user.SignupRequestDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.enums.UserRole;
import com.teamnine.humanofdelivery.enums.UserStatus;
import com.teamnine.humanofdelivery.exception.user.UserErrorCode;
import com.teamnine.humanofdelivery.exception.user.UserException;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 회원 관리와 관련된 비즈니스 로직을 제공하는 서비스 클래스.
 * @author 이빛나
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final SessionUtils sessionUtils;

    /**
     * 회원가입 기능을 제공합니다.
     *
     * @param dto 회원가입 요청 데이터 (이메일, 비밀번호, 이름 포함)
     * @return 생성된 회원 정보를 담은 UserResponseDto
     * @throws UserException 이메일 중복, 비밀번호 또는 이름 검증 실패 시 예외 발생
     * @apiNote 비밀번호는 암호화된 상태로 저장됩니다.
     */
    @Transactional
    public UserResponseDto signUp(SignupRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getName(), dto.getEmail(), encodedPassword, dto.getRole());
        Member saveMember = memberRepository.save(member);
        return UserResponseDto.toDto(saveMember);
    }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param dto 로그인 요청 데이터 (이메일과 비밀번호)
     * @return 로그인된 회원 정보를 담은 UserResponseDto
     * @throws UserException 이메일 또는 비밀번호가 일치하지 않을 경우 예외 발생
     * @apiNote 유효성 검증 후 세션에 사용자 정보를 저장해야 합니다.
     */
    public UserResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByEmailOrElseThrow(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_INCORRECT);
        }
        return UserResponseDto.toDto(member);
    }

    /**
     * 로그아웃 요청을 처리합니다.
     *
     * @param request 현재 HTTP 요청 객체
     * @return 로그아웃 완료 메시지
     * @apiNote 현재 세션이 존재하면 무효화합니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 완료");
    }

    /**
     * 특정 회원의 프로필 정보를 조회합니다.
     *
     * @param userId 조회하려는 회원의 ID
     * @return 회원의 프로필 정보를 담은 UserResponseDto 또는 OwnerResponseDto
     * @throws UserException 회원이 존재하지 않을 경우 예외 발생
     * @apiNote 회원의 역할에 따라 반환 형식이 달라집니다.
     */
    public Object findUserById(Long userId) {
        Member member = memberRepository.findByIdOrElseThrow(userId);

        if(UserRole.USER.equals(member.getRole())) {
            return UserResponseDto.toDto(member);
        }
        if(UserRole.OWNER.equals(member.getRole())) {
            long activeStoreCount = storeRepository.countActiveStoresByOwnerId(member.getUserId());
            List<Store> storeList = storeRepository.findAllByOwnerId(member.getUserId());
            List<OwnerResponseDto.StoreDetail> storeDetails = storeList.stream()
                    .map(store -> new OwnerResponseDto.StoreDetail(
                            store.getId(),
                            store.getName(),
                            store.getStatus()
                    ))
                    .toList();
            return new OwnerResponseDto(
                    member.getName(),
                    member.getEmail(),
                    member.getRole(),
                    member.getCreatedDate(),
                    member.getModifiedDate(),
                    activeStoreCount,
                    storeDetails
            );
        }

        return UserResponseDto.toDto(member);
    }

    /**
     * 회원 프로필 정보를 수정합니다.
     *
     * @param userId  수정하려는 회원의 ID
     * @param updates 수정할 데이터 (이름, 이메일, 비밀번호 등)
     * @param request 현재 HTTP 요청 객체
     * @return 수정된 회원 정보를 담은 UserResponseDto
     * @throws UserException 입력값이 잘못되었거나 권한이 없을 경우 예외 발생
     * @apiNote 수정 가능한 키(name, email, password)만 허용됩니다.
     */
    public UserResponseDto updateUserById(Long userId, Map<String, Object> updates, HttpServletRequest request) {
        Member findMember = memberRepository.findByIdOrElseThrow(userId);
        sessionUtils.checkAuthorization(findMember);

        updates.forEach((key, value) -> {
            if (value == null) {
                throw new UserException(UserErrorCode.RESPONSE_INCORRECT);
            }

            switch (key) {
                case "name" -> findMember.setName((String) value);
                case "email" -> findMember.setEmail((String) value);
                case "password" -> findMember.setPassword(passwordEncoder.encode((String) value));
                default -> throw new UserException(UserErrorCode.RESPONSE_INCORRECT);
            }
        });
        memberRepository.save(findMember);
        return UserResponseDto.toDto(findMember);
    }

    /**
     * 회원 탈퇴를 처리합니다.
     *
     * @param userId 탈퇴하려는 회원의 ID
     * @throws UserException 이미 탈퇴한 회원이거나 권한이 없을 경우 예외 발생
     * @apiNote 회원 탈퇴 시 상태를 "DELETED"로 변경합니다.
     */
    // todo 리뷰 및 주문 기능과 연계하여 탈퇴 시 이름 수정 필요
    public void deleteUserById(Long userId) {
        Member findMember = memberRepository.findByIdOrElseThrow(userId);
        sessionUtils.checkAuthorization(findMember);

        if (findMember.getStatus() != UserStatus.DELETED) {
            findMember.setStatus(UserStatus.DELETED);

//            // 회원이 작성한 리뷰와 주문을 "탈퇴한 회원"으로 업데이트
//            findMember.getReviews().forEach(review -> {
//                review.setAuthorName("탈퇴한 회원");
//                review.setMember(null);
//            });
//
//            findMember.getOrders().forEach(order -> {
//                order.setOrderedBy("탈퇴한 회원");
//                order.setMember(null);
//            });

            memberRepository.save(findMember);
        } else {
            throw new UserException(UserErrorCode.USER_DEACTIVATED);
        }
    }
}