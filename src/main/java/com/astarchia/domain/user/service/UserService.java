package com.astarchia.domain.user.service;


import com.astarchia.domain.user.dto.request.UserCreateRequestDTO;
import com.astarchia.domain.user.dto.response.UserResponseDTO;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        // UserCreateRequestDTO (email, loginId, password, nickname)

        // DB에 이미 있는지 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // DTO → Entity 변환 (생성자대신 builder())
        Users user = Users.builder()
                .email(request.getEmail())
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

        // 3단계: 비밀번호 암호화
        // → BCryptPasswordEncoder 사용 나중에

        // DB 저장
        Users savedUser = userRepository.save(user);

        // Entity → DTO 변환해 응답
        return UserResponseDTO.from(savedUser);
    }


}
