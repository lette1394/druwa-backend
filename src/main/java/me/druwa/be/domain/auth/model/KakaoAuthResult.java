package me.druwa.be.domain.auth.model;

import lombok.Data;

@Data
public class KakaoAuthResult {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;
}
