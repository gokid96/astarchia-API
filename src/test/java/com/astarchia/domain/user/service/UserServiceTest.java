package com.astarchia.domain.user.service;

import com.astarchia.domain.user.dto.request.UserCreateRequestDTO;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입")
    void createUser() {
        //given
        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .email("test@test.com")
                .loginId("test")
                .password("test123!")
                .nickname("tester")
                .build();

        Users user = Users.builder()
                .userId(1L)
                .email("test@test.com")
                .loginId("test")
                .password("test123!")
                .nickname("tester")
                .build();

        //Mock



    }

}
