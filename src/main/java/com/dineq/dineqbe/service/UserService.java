package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.UserEntity;
import com.dineq.dineqbe.domain.enums.UserType;
import com.dineq.dineqbe.dto.user.SignUpRequestDTO;
import com.dineq.dineqbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * POST /api/v1/auth/register
     * @param dto
     */
    public void signUpAsUser(SignUpRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userType(UserType.USER)  // 기본 USER
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    /**
     * 관리자 회원가입
     * POST /api/v1/auth/register/admin
     * @param dto
     */
    public void signUpAsAdmin(SignUpRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userType(UserType.ADMIN)  // 기본 USER
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    /**
     * 회원 탈퇴
     * DELETE /api/v1/auth/profile/{userId}
     * @param userId
     */
    public void deleteUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
                );

        userRepository.delete(user);
    }
}
