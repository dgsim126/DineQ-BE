package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.UserEntity;
import com.dineq.dineqbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 이미 암호화된 비밀번호
                .roles(user.getUserType().name()) // USER, ADMIN
                .build();
    }
}
