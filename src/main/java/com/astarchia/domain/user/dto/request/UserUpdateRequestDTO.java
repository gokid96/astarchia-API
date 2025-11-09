package com.astarchia.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    @Size(min = 2, max = 50, message = "닉네임은 2-50자여야 합니다")
    private String nickname;

    @Size(max = 20, message = "비밀번호는 최대 20자입니다")
    private String password;  // 비밀번호 변경 시
}