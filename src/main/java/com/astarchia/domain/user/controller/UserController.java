package com.astarchia.domain.user.controller;


import com.astarchia.domain.user.dto.request.UserCreateRequestDTO;
import com.astarchia.domain.user.dto.request.UserLoginRequestDTO;
import com.astarchia.domain.user.dto.request.UserUpdateRequestDTO;
import com.astarchia.domain.user.dto.response.UserResponseDTO;
import com.astarchia.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    // 로그인 (AuthController 만들어서 이동 시키기 )
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // 조회 @AuthenticationPrincipal 로 인증정보 넘기는거로 수정하기
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    // 탈퇴 @AuthenticationPrincipal 로 인증정보 넘기는거로 수정하기
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
