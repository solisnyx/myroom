package com.myroom.auth;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myroom.user.AuthProvider;
import com.myroom.user.User;
import com.myroom.user.UserRepository;
import com.myroom.user.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();

        if (!"kakao".equalsIgnoreCase(registrationId)) {
            throw new OAuth2AuthenticationException(
                    "Unsupported OAuth2 provider: " + registrationId);
        }

        KakaoUserInfo info = KakaoUserInfo.from(oauth2User.getAttributes());
        User user = userRepository
                .findByProviderAndProviderId(AuthProvider.KAKAO, info.providerId())
                .map(existing -> updateProfile(existing, info))
                .orElseGet(() -> userRepository.save(createUser(info)));

        return new CustomOAuth2User(user, oauth2User.getAttributes());
    }

    private User createUser(KakaoUserInfo info) {
        return User.builder()
                .email(info.email())
                .nickname(info.nickname())
                .role(UserRole.USER)
                .provider(AuthProvider.KAKAO)
                .providerId(info.providerId())
                .profileImageUrl(info.profileImageUrl())
                .build();
    }

    private User updateProfile(User user, KakaoUserInfo info) {
        if (info.nickname() != null) {
            user.setNickname(info.nickname());
        }
        if (info.profileImageUrl() != null) {
            user.setProfileImageUrl(info.profileImageUrl());
        }
        if (info.email() != null && user.getEmail() == null) {
            user.setEmail(info.email());
        }
        return user;
    }

    private record KakaoUserInfo(String providerId, String email, String nickname, String profileImageUrl) {

        @SuppressWarnings("unchecked")
        static KakaoUserInfo from(Map<String, Object> attributes) {
            String providerId = String.valueOf(attributes.get("id"));

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            Map<String, Object> profile = kakaoAccount == null ? null
                    : (Map<String, Object>) kakaoAccount.get("profile");

            String email = kakaoAccount == null ? null : (String) kakaoAccount.get("email");
            String nickname = firstNonNull(
                    profile == null ? null : (String) profile.get("nickname"),
                    properties == null ? null : (String) properties.get("nickname"));
            String profileImageUrl = firstNonNull(
                    profile == null ? null : (String) profile.get("profile_image_url"),
                    properties == null ? null : (String) properties.get("profile_image"));

            if (nickname == null) {
                nickname = "kakao_" + providerId;
            }

            return new KakaoUserInfo(providerId, email, nickname, profileImageUrl);
        }

        private static String firstNonNull(String a, String b) {
            return a != null ? a : b;
        }
    }
}
