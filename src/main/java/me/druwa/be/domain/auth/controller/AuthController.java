package me.druwa.be.domain.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import me.druwa.be.domain.auth.model.KakaoAuthResult;
import me.druwa.be.domain.auth.model.KakaoProfile;
import me.druwa.be.domain.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.groovy.util.Maps;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RestTemplate restTemplate;
    private final KakaoAuthService kakaoAuthService;

    @Value("${social.kakao.url.login}")
    private String url;

    @Value("${social.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirect;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> socialLogin() {
        final String loginUrl = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s%s",
                                              url,
                                              kakaoClientId,
                                              baseUrl,
                                              kakaoRedirect);

        return ResponseEntity.ok(Maps.of("url", loginUrl));
    }

    @GetMapping(value = "/oauth/kakao/redirect")
    public ResponseEntity<?> redirectKakao(@RequestParam String code) {
        final KakaoAuthResult kakaoTokenInfo = kakaoAuthService.populateAndGetKakaoTokenInfo(code);
        final KakaoProfile kakaoProfile = kakaoAuthService.getKakaoProfile(kakaoTokenInfo.getAccess_token());
        return ResponseEntity.ok(kakaoTokenInfo);
    }

    @GetMapping("/oauth2/callback/**")
    public ResponseEntity<?> auth() {
        return ResponseEntity.ok().build();
    }
}
