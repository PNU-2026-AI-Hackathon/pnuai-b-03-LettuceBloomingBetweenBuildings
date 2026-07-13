package ai.pnu.b03.minifarm.config.oauth;

import ai.pnu.b03.minifarm.config.auth.PrincipalDetails;
import ai.pnu.b03.minifarm.config.oauth.provider.GoogleUserInfo;
import ai.pnu.b03.minifarm.config.oauth.provider.KakaoUserInfo;
import ai.pnu.b03.minifarm.config.oauth.provider.NaverUserInfo;
import ai.pnu.b03.minifarm.config.oauth.provider.OAuth2UserInfo;
import ai.pnu.b03.minifarm.entity.User;
import ai.pnu.b03.minifarm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 구글 등 외부 사이트로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Transactional // 추가해줌
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 구글 로그인 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원 프로필 받기 위해 loadUser 함수 호출 -> 회원 프로필
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원가입 연동 진행
        User user = registerUser(userRequest, oAuth2User);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private User registerUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else if (registrationId.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.warn("not supported login request");
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

//        String username = provider + '_' + providerId; // google_12421528902251...

        String email = oAuth2UserInfo.getEmail();
        String username = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode("dummy"); // 의미 없는데 그냥 넣어는 주기 위해
        String provider = oAuth2UserInfo.getProvider(); // google, naver etc..
        String providerId = oAuth2UserInfo.getProviderId();
        String role = "ROLE_USER";

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(user);
        } else {
            // OAuth2SuccessHandler 호출
            log.info("이미 가입된 회원이 있습니다.");
        }

        return user;
    }
}
