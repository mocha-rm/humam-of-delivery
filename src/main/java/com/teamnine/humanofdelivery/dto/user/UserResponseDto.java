package com.teamnine.humanofdelivery.dto.user;

import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final String name;

    private final String email;

    private final UserRole role;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public static UserResponseDto toDto(Member member) {
        return new UserResponseDto(
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getCreatedDate(),
                member.getModifiedDate()
        );
    }
}
