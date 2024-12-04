package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.dto.user.LoginRequestDto;
import com.teamnine.humanofdelivery.dto.user.SignupRequestDto;
import com.teamnine.humanofdelivery.dto.user.MemberResponseDto;
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

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // todo 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> createUser(
            @Valid
            @RequestBody SignupRequestDto dto
    ) {
        return ResponseEntity.ok().body(memberService.signUp(dto));
    }

    // todo 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto dto,
            HttpServletRequest request
    ) {
        MemberResponseDto userResponse = memberService.login(dto);
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionNames.USER_AUTH, dto.getEmail());
        return ResponseEntity.ok(userResponse);
    }

    // todo 로그아웃
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

    // todo 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findUser(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(memberService.findUserById(id));
    }

    // todo 프로필 수정
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody Map<String, Object> updates,
            HttpServletRequest request) {
        return new ResponseEntity<>(memberService.updateUserById(id, updates), HttpStatus.OK);
    }

    // todo 회원 탈퇴
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        memberService.deleteUserById(id);
        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }
}
