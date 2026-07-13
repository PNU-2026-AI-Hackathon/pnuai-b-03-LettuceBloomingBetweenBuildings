//package ai.pnu.b03.minifarm.config;
//
//import ai.pnu.b03.minifarm.config.oauth.PrincipalOauth2UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity // 활성화되면 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
//@RequiredArgsConstructor
//public class SecurityConfigLegacy {
//
//    private final PrincipalOauth2UserService principalOauth2UserService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // CSRF 해제
//                .csrf(AbstractHttpConfigurer::disable)
//
//                // 권한 설정
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/user/**").authenticated() // 인증만 되면 OK
//                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().permitAll() // 나머지는 다 접근 허용
//                )
//
//                // 폼 로그인 설정
//                .formLogin(form -> form
//                        .loginPage("/loginForm") // 권한 없는 페이지 접속 시 튕겨낼 로그인 주소
//                        .loginProcessingUrl("/login") // 로그인 진행 주소. 별도 로그인 페이지 없으면 시큐리티가 낚아채서 대신 로그인을 진행
//                        .defaultSuccessUrl("/") // 로그인 성공 시 이동할 주소
//                )
//
//                // OAuth2 로그인 설정
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/loginForm")
//                        // 구글 로그인 완료된 후의 후처리가 필요함.
//                        // 1. 코드받기(인증),
//                        // 2. 엑세스토큰(권한),
//                        // 3. 사용자프로필 정보 가져오기,
//                        // 4-1. 그 정보를 토대로 자동 회원가입 진행 가능
//                        // 4-2. (이메일, 전화번호, 이름, 아이디) 등 정보 외에 추가적인 정보 구성을 해서 회원가입 진행 가능
//                        // OAuth Client를 쓰면 엑세스토큰+사용자프로필 정보를 같이 가져와줌
//                        .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
//                );
//
//        return http.build();
//    }
//}
