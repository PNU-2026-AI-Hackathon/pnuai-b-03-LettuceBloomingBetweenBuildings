package ai.pnu.b03.minifarm.config;

import ai.pnu.b03.minifarm.config.jwt.JwtAuthorizationFilter;
import ai.pnu.b03.minifarm.config.oauth.OAuth2SuccessHandler;
import ai.pnu.b03.minifarm.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // Form 로그인, Basic HTTP 인증 비활성화 (앱 API 서버이므로 불필요)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션을 사용 X (STATELESS 설정)~
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 경로별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/login",
                                "/api/join",
                                "/loginForm",
                                "/joinForm",
                                "/loginSuccess").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )

                // JWT 필터 등록 (기존 UsernamePasswordAuthenticationFilter 전에 실행)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // userInfoEndpoint: 구글 등에서 정보 가져오면 PrincipalOauth2UserService가 처리
                        .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
                        // successHandler: 처리 후 JWT를 만들어 Flutter로 리다이렉트 (세션X)
                        .successHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }
}