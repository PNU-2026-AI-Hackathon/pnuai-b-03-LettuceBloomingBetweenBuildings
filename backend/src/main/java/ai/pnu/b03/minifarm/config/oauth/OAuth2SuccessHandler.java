package ai.pnu.b03.minifarm.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ai.pnu.b03.minifarm.config.auth.PrincipalDetails;
import ai.pnu.b03.minifarm.config.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String id = principalDetails.getUser().getId();
        String username = principalDetails.getUser().getUsername();
        String role = principalDetails.getUser().getRole();

        // JWT 토큰 생성
        String token = jwtUtil.createToken(id, username, role);
        log.info("OAuth2 Login Success! Generated Token: {}", token);

        // 리다이렉트 주소 설정
        // 플러터 연동용
        // String targetUrl = "minifarm://login?token=" + token;

        // 웹 브라우저 테스트용
        String targetUrl = "/loginSuccess?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}