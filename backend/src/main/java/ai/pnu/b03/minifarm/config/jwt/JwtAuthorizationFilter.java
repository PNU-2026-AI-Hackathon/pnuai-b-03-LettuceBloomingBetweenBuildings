package ai.pnu.b03.minifarm.config.jwt;

import ai.pnu.b03.minifarm.config.auth.PrincipalDetails;
import ai.pnu.b03.minifarm.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 Authorization 추출
        String authorization = request.getHeader("Authorization");

        // 토큰이 없거나 Bearer로 시작하지 않으면 통과 (권한이 필요한 요청이면 SecurityConfig에서 걸러짐)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 문자열 제거하고 순수 토큰만 추출
        String token = authorization.split(" ")[1];

        // 토큰 만료되었는지 확인
        if (jwtUtil.isExpired(token)) {
            log.info("Token Expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 유저 정보 추출하여 임시 User 객체 생성
        String id = jwtUtil.getId(token);
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setRole(role);

        // SecurityContext에 인증 정보 저장 (이 요청에 한해 로그인된 것으로 처리)
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}