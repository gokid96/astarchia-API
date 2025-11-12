package com.astarchia.domain.user.service;

import com.astarchia.domain.user.dto.request.UserCreateRequestDTO;
import com.astarchia.domain.user.dto.request.UserLoginRequestDTO;
import com.astarchia.domain.user.dto.request.UserUpdateRequestDTO;
import com.astarchia.domain.user.dto.response.UserResponseDTO;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /*
    * 생성
    * */
    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        Users savedUser = userRepository.save(user);
        return UserResponseDTO.from(savedUser);
    }
    /*
    * 로그인
    * */
    public UserResponseDTO login(UserLoginRequestDTO request) {
        Users user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return UserResponseDTO.from(user);
    }
    /*
    * 조회
    * */
    public UserResponseDTO getUserInfo(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDTO.from(user);
    }
    /*
    * 수정
    * */
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO request) {
        // user의 스냅샷을 1차 캐시에 저장
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 엔티티 값 변경
        user.updatePassword(passwordEncoder.encode(request.getPassword()));
        user.updateNickname(request.getNickname());

        return UserResponseDTO.from(user);
        //메서드 종료 → 트랜잭션 커밋 시점에 dirty checking 변경 감지 & UPDATE 쿼리 실행
    }
    /*
    * 삭제
    * */
    @Transactional
    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}