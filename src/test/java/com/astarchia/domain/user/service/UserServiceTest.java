package com.astarchia.domain.user.service;

import com.astarchia.domain.user.dto.request.UserCreateRequestDTO;
import com.astarchia.domain.user.dto.response.UserResponseDTO;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void createUser() {
        // ===== 1. 실제 요청처럼 가입 정보 입력 후 객체 생성 =====
        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .email("test@test.com")
                .loginId("test")
                .password("test123!")
                .nickname("tester")
                .build();
        // → "사용자가 회원가입 폼에 입력한 정보"

        // ===== 2. 가짜로 저장된 객체 생성 =====
        Users savedUser = Users.builder()
                .userId(1L)  // DB가 자동 생성할 ID
                .email("test@test.com")
                .loginId("test")
                .password("test123!")
                .nickname("tester")
                .build();
        // → "DB에 저장되면 이렇게 나올 거야"

        // ===== 3. Service에서 검사하는 부분들 미리 가짜 응답 만들기 =====

        // "이메일 중복 체크? 중복 아님!"
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // "로그인ID 중복 체크? 중복 아님!"
        when(userRepository.existsByLoginId(anyString())).thenReturn(false);

        // "닉네임 중복 체크? 중복 아님!"
        when(userRepository.existsByNickname(anyString())).thenReturn(false);

        // "저장? savedUser 리턴!"
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        // ===== 4. 실제 Service에 회원가입 요청 =====
        UserResponseDTO response = userService.createUser(request);
        // Service가 위에서 설정한 가짜 응답들 받으면서 실행됨

        // ===== 5. 제대로 동작했나 검증 =====
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getLoginId()).isEqualTo("test");
        assertThat(response.getNickname()).isEqualTo("tester");
        verify(userRepository).save(any(Users.class));
        // → "결과가 예상과 같은지 확인"
    }

    @Test
    @DisplayName("중복 실패")
    void duplicateEmail() {
        // given
        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .email("dup@test.com")
                .loginId("testuser")
                .password("password123")
                .nickname("테스터")
                .build();

        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 이메일입니다.");

        verify(userRepository, never()).save(any());
    }
}