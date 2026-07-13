package ai.pnu.b03.minifarm.controller.auth;

import ai.pnu.b03.minifarm.common.exception.CustomException;
import ai.pnu.b03.minifarm.common.exception.ErrorCode;
import ai.pnu.b03.minifarm.config.jwt.JwtUtil;
import ai.pnu.b03.minifarm.dto.ApiResponse;
import ai.pnu.b03.minifarm.dto.user.LoginRequestDto;
import ai.pnu.b03.minifarm.entity.User;
import ai.pnu.b03.minifarm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());

        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 인증 성공 시 JWT 토큰 생성
        String token = jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());

        // 토큰 JSON 형태로 응답
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
