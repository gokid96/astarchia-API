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
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateRequestDTO request) {
        UserResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        UserResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    // 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserInfo(@PathVariable Long userId) {
        UserResponseDTO response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }
    // 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequestDTO request){
        UserResponseDTO response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }
    // 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
