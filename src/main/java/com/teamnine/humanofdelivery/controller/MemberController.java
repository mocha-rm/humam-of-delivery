package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.dto.user.LoginRequestDto;
import com.teamnine.humanofdelivery.dto.user.SignupRequestDto;
import com.teamnine.humanofdelivery.dto.user.UserResponseDto;
import com.teamnine.humanofdelivery.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원 관리와 관련된 REST API를 제공하는 컨트롤러 클래스.
 * @author 이빛나
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param dto 회원가입 요청 데이터
     * @return 생성된 회원 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid
            @RequestBody SignupRequestDto dto
    ) {
        return ResponseEntity.ok().body(memberService.signUp(dto));
    }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param dto     로그인 요청 데이터
     * @param request HTTP 요청 객체
     * @return 로그인된 회원 정보
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto dto,
            HttpServletRequest request
    ) {
        UserResponseDto userResponse = memberService.login(dto);
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionNames.USER_AUTH, dto.getEmail());
        return ResponseEntity.ok(userResponse);
    }

    /**
     * 로그아웃 요청을 처리합니다.
     *
     * @param request HTTP 요청 객체
     * @return 로그아웃 완료 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 완료");
    }

    /**
     * 특정 회원의 프로필 정보를 조회합니다.
     *
     * @param id 조회할 회원 ID
     * @return 회원 프로필 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(memberService.findUserById(id));
    }

    /**
     * 특정 회원의 프로필 정보를 수정합니다.
     *
     * @param id      수정할 회원 ID
     * @param updates 수정할 데이터
     * @param request HTTP 요청 객체
     * @return 수정된 회원 정보
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody Map<String, Object> updates,
            HttpServletRequest request) {
        return new ResponseEntity<>(memberService.updateUserById(id, updates, request), HttpStatus.OK);
    }

    /**
     * 특정 회원의 계정을 비활성화합니다.
     *
     * @param id 비활성화할 회원 ID
     * @return 비활성화 완료 메시지
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {
        memberService.deleteUserById(id);
        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }
}
