package ai.pnu.b03.minifarm.config.oauth.provider;

public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
