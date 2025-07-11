package com.controller;

import com.dto.UserReqDto;
import com.entity.User;
import com.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/sign-up")
    public String signup(@RequestBody UserReqDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            return "이미 존재하는 아이디입니다.";
        }
        userRepository.save(User.builder()
                .username(dto.username())
                .password(dto.password())
                .nickname(dto.nickname())
                .build());
        return "회원가입 성공";
    }

    // 로그인 (세션)
    @PostMapping("/sign-in")
    public String signin(@RequestBody UserReqDto dto, HttpSession session) {
        var optional = userRepository.findByUsername(dto.username());
        if (optional.isPresent() && optional.get().getPassword().equals(dto.password())) {
            session.setAttribute("loginUser", optional.get().getId());
            return "로그인 성공";
        }
        return "아이디 또는 비밀번호가 틀렸습니다.";
    }

    // 로그인 체크
    @GetMapping("/me")
    public String me(HttpSession session) {
        Object userId = session.getAttribute("loginUser");
        return (userId != null) ? "로그인된 사용자 ID: " + userId : "로그인 안 됨";
    }

    // 로그아웃
    @PostMapping("/sign-out")
    public String signout(HttpSession session) {
        session.invalidate();
        return "로그아웃 완료";
    }
}
