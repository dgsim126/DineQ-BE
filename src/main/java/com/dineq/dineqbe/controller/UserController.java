package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.user.SignUpRequestDTO;
import com.dineq.dineqbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * POST /api/v1/auth/register
     * @param dto
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> signUpAsUser(@RequestBody SignUpRequestDTO dto) {
        try {
            userService.signUpAsUser(dto);
            return ResponseEntity.ok("회원가입 성공");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    /**
     * 관리자 회원가입
     * POST /api/v1/auth/register/admin
     * @param dto
     * @return
     */
    @PostMapping("/register/admin")
    public ResponseEntity<String> signUpAsAdmin(@RequestBody SignUpRequestDTO dto) {
        try {
            userService.signUpAsAdmin(dto);
            return ResponseEntity.ok("관리자 회원가입 성공");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @return
     */
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("회원 탈퇴 완료");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    /**
     * 로그인 여부 확인
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkLoginStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 경우 (로그인 안 되어있는 경우)
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("로그인 안됨");
        }

        // 인증된 경우 (로그인 되어있는 경우)
        return ResponseEntity.ok("로그인 됨 (사용자: " + auth.getName() + ")");
    }

}
