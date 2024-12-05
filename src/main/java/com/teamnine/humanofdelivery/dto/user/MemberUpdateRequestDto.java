package com.teamnine.humanofdelivery.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequestDto {

    @NotBlank
    @Size(min = 2, max = 10)
    private final String name;

    @NotBlank
    @Pattern(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$", message = "올바르지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 20자 까지 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.")
    private final String password;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 20자 까지 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.")
    private final String newPassword;

}
