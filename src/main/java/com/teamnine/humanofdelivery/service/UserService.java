package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.config.Password.PasswordEncoder;
import com.teamnine.humanofdelivery.dto.user.LoginRequestDto;
import com.teamnine.humanofdelivery.dto.user.SignupRequestDto;
import com.teamnine.humanofdelivery.dto.user.UserResponseDto;
import com.teamnine.humanofdelivery.entity.User;
import com.teamnine.humanofdelivery.enums.UserStatus;
import com.teamnine.humanofdelivery.exception.user.UserErrorCode;
import com.teamnine.humanofdelivery.exception.user.UserException;
import com.teamnine.humanofdelivery.repository.UserRepository;
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
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * @apiNote 회원가입
     * @param dto (이메일, 비밀번호, 이름)
     * @return UserResponseDto, HttpStatus.OK
     * @throws UserException (이메일, 비밀번호, 이름 검증에 관련된 예외 처리)
     */
    @Transactional
    public UserResponseDto signUp(SignupRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getName(), dto.getEmail(), encodedPassword, dto.getRole());
        User saveUser = userRepository.save(user);
        return UserResponseDto.toDto(saveUser);
    }

    /**
     * @apiNote 로그인
     * @param dto (이메일, 패스워드)
     * @return "로그인 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (이메일 또는 비밀번호가 일치하지 않을 시 예외 출력)
     */
    public UserResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmailOrElseThrow(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_INCORRECT);
        }
        return UserResponseDto.toDto(user);
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
     * @return UserResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 예외 발생
     */
    public UserResponseDto findUserById(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.toDto(user);
    }

    // 프로필 수정 기능
    /**
     * @apiNote 프로필 수정 기능
     * @param userId  유저 아이디
     * @return userResponseDto (HttpStatus.OK) / 로그인한 유저와 조회유저가 다른 경우 입력값이 없는 경우 예외 발생
     */
    // todo 프로필 수정
    public UserResponseDto updateUserById(Long userId, Map<String, Object> updates
    ) {
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Map<String, Consumer<Object>> updateActions = Map.of(
                "username", value -> findUser.setName((String) value),
                "email", value -> findUser.setEmail((String) value),
                "password", value -> findUser.setPassword(passwordEncoder.encode((String) value))
        );
        updates.forEach((key, value) -> {
            Consumer<Object> action = updateActions.get(key);
            if (action != null) {
                action.accept(value);
            } else {
                throw new UserException(UserErrorCode.RESPONSE_INCORRECT);
            }
        });
        return UserResponseDto.toDto(findUser);
    }

    /**
     * @apiNote 회원 탈퇴 기능
     * @param userId  (유저 아이디)
     * @return "회원탈퇴 완료" 문자열 반환 (HttpStatus.OK)
     * @throws UserException (비밀번호가 일치하지 않거나 이미 탈퇴한 회원인 경우 예외 발생)
     */
    public void deleteUserById(Long userId) {
        User findUser = userRepository.findByIdOrElseThrow(userId);

        // 상태를 DELETED로 변경
        if (findUser.getStatus() != UserStatus.DELETED) {
            findUser.setStatus(UserStatus.DELETED);
            userRepository.save(findUser);
        } else {
            throw new UserException(UserErrorCode.USER_DEACTIVATED);
        }
    }
}
