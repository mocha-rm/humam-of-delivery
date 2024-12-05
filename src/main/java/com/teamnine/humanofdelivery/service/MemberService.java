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
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final SessionUtils sessionUtils;

    /**
     * @param dto (이메일, 비밀번호, 이름)
     * @return UserResponseDto, HttpStatus.OK
     * @throws UserException (이메일, 비밀번호, 이름 검증에 관련된 예외 처리)
     * @apiNote 회원가입
     */
    @Transactional
    public UserResponseDto signUp(SignupRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getName(), dto.getEmail(), encodedPassword, dto.getRole());
        Member saveMember = memberRepository.save(member);
        return UserResponseDto.toDto(saveMember);
    }

    /**
     * @param dto (이메일, 패스워드)
     * @return "로그인 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (이메일 또는 비밀번호가 일치하지 않을 시 예외 출력)
     * @apiNote 로그인
     */
    public UserResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByEmailOrElseThrow(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_INCORRECT);
        }
        return UserResponseDto.toDto(member);
    }

    /**
     * @param request (HttpSession.getSession) 로그인 된 세션이 있는 지 체크
     * @return "로그아웃 완료" 문자열 반환
     * @apiNote 로그아웃
     * @apiNote 로그아웃
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
     * @param userId 유저 식별자
     * @return UserResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 예외 발생
     * @apiNote 프로필 조회 기능
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

    // 프로필 수정 기능

    /**
     * @param userId  유저 아이디
     * @param request
     * @return userResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 입력값이 없는 경우 예외 발생
     * @apiNote 프로필 수정 기능
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
     * @param userId (유저 아이디)
     * @return "회원탈퇴 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (비밀번호가 일치하지 않거나 이미 탈퇴한 회원인 경우 예외 발생)
     * @apiNote 회원 탈퇴 기능
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