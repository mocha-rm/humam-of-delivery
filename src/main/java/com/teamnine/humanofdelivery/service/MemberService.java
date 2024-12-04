package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.config.Password.PasswordEncoder;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.user.LoginRequestDto;
import com.teamnine.humanofdelivery.dto.user.MemberResponseDto;
import com.teamnine.humanofdelivery.dto.user.SignupRequestDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.enums.UserStatus;
import com.teamnine.humanofdelivery.exception.user.UserErrorCode;
import com.teamnine.humanofdelivery.exception.user.UserException;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SessionUtils sessionUtils;

    /**
     * @apiNote 회원가입
     * @param dto (이메일, 비밀번호, 이름)
     * @return MemberResponseDto, HttpStatus.OK
     * @throws UserException (이메일, 비밀번호, 이름 검증에 관련된 예외 처리)
     */
    @Transactional
    public MemberResponseDto signUp(SignupRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getName(), dto.getEmail(), encodedPassword, dto.getRole());
        Member saveMember = memberRepository.save(member);
        return MemberResponseDto.toDto(saveMember);
    }

    /**
     * @apiNote 로그인
     * @param dto (이메일, 패스워드)
     * @return "로그인 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (이메일 또는 비밀번호가 일치하지 않을 시 예외 출력)
     */
    public MemberResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByEmailOrElseThrow(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_INCORRECT);
        }
        return MemberResponseDto.toDto(member);
    }

    /**
     * @apiNote 로그아웃
     * @param request (HttpSession.getSession) 로그인 된 세션이 있는 지 체크
     * @return "로그아웃 완료" 문자열 반환
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

    // 프로필 조회 기능
    /**
     * @apiNote 프로필 조회 기능
     * @param userId 유저 식별자
     * @return MemberResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 예외 발생
     */
    public MemberResponseDto findUserById(Long userId) {
        Member member = memberRepository.findByIdOrElseThrow(userId);
        return MemberResponseDto.toDto(member);
    }

    // 프로필 수정 기능
    /**
     * @apiNote 프로필 수정 기능
     * @param userId  유저 아이디
     * @return userResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 입력값이 없는 경우 예외 발생
     */
    public MemberResponseDto updateUserById(Long userId, Map<String, Object> updates) {
        Member findMember = memberRepository.findByIdOrElseThrow(userId);
        sessionUtils.checkAuthorization(findMember);
        Map<String, Consumer<Object>> updateActions = Map.of(
                "name", value -> findMember.setName((String) value),
                "email", value -> findMember.setEmail((String) value),
                "password", value -> findMember.setPassword(passwordEncoder.encode((String) value))
        );
        updates.forEach((key, value) -> {
            if (value == null) {
                throw new UserException(UserErrorCode.RESPONSE_INCORRECT);
            }
            Consumer<Object> action = updateActions.get(key);
            if (action != null) {
                action.accept(value);
            } else {
                throw new UserException(UserErrorCode.RESPONSE_INCORRECT);
            }
        });
        memberRepository.save(findMember);
        return MemberResponseDto.toDto(findMember);
    }

    /**
     * @apiNote 회원 탈퇴 기능
     * @param userId  (유저 아이디)
     * @return "회원탈퇴 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (비밀번호가 일치하지 않거나 이미 탈퇴한 회원인 경우 예외 발생)
     */
    public void deleteUserById(Long userId) {
        Member findMember = memberRepository.findByIdOrElseThrow(userId);
        sessionUtils.checkAuthorization(findMember);

        // 상태를 DELETED로 변경
        if (findMember.getStatus() != UserStatus.DELETED) {
            findMember.setStatus(UserStatus.DELETED);
            memberRepository.save(findMember);
        } else {
            throw new UserException(UserErrorCode.USER_DEACTIVATED);
        }
    }
}
