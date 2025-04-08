package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.user.SignUpRequestDTO;
import com.dineq.dineqbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * @param dto
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO dto) {
        try {
            userService.signUp(dto);
            return ResponseEntity.ok("회원가입 성공");
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
}
