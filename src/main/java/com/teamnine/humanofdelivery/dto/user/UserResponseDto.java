package com.teamnine.humanofdelivery.dto.user;

import com.teamnine.humanofdelivery.config.role.MemberRole;
import com.teamnine.humanofdelivery.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final String name;

    private final String email;

    private final MemberRole role;

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
