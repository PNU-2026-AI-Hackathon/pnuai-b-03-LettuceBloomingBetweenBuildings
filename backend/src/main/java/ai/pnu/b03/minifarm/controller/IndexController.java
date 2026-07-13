package ai.pnu.b03.minifarm.controller;

import ai.pnu.b03.minifarm.config.auth.PrincipalDetails;
import ai.pnu.b03.minifarm.entity.User;
import ai.pnu.b03.minifarm.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            // 두 가지 방법 (객체 타입 매핑)으로 Security Session의 유저 정보 받아올 수 있음
            Authentication authentication,
            @AuthenticationPrincipal UserDetails userDetails ) {
        log.info("/test/login");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info(principalDetails.getUser().toString());

        log.info(userDetails.getUsername());

        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(
            // OAuth에 대해서도 아래 두 객체 타입으로 받아올 수 있음
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth2User ) {
        log.info("/test/oauth/login");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info(oAuth2User.getAttributes().toString());
        log.info(oauth2User.getAttributes().toString());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info(principalDetails.toString());
        log.info(principalDetails.getUser().toString());
        log.info(principalDetails.getUsername());
        log.info(principalDetails.getAuthorities().toString());

        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 낚아챔
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/api/join")
    public String join(@ModelAttribute User user) {
        userService.saveUser(user);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess"; // loginSuccess.html을 반환
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 함수 호출 전 (잘 안씀)
//    @PostAuthorize() // 함수 호출 후 인증 (거의 안씀)
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
