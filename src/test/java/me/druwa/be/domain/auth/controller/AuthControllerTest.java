package me.druwa.be.domain.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.specification.RequestSpecification;
import me.druwa.be.docs.DocsUtils;
import me.druwa.be.util.AutoSpringBootTest;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@Rollback
@AutoSpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port)
                        .config(config().redirect(redirectConfig().followRedirects(false)
                                                                  .and()
                                                                  .maxRedirects(0)));
    }

    @Test
    void kakao() {
        String regex = "https:\\/\\/kauth\\.kakao\\.com\\/oauth\\/authorize\\?response_type=code&client_id=\\w+&state=.*&redirect_uri=.*";
        given(spec).that()
                   .filter(document("auth__kakao", requestParameters(
                           parameterWithName("redirect_uri").description(
                                   "oauth가 종료된 이후 redirect 될 endpoint.\n이후 ?token={token} 형태로 redirect url이 생성됩니다.\n e.g. https://druwa.netlify.com/oauth/check?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTc4NTAzNDg1LCJleHAiOjE1NzkzNjc0ODV9.5h4iW5CET9prk-hxTxOg3DeuPfHpT9TUb-7OQ6cKZHGRBIsRW7C3sRtZydSuzm_B_K5fzUYaAYLRdyBoHjxPDQ")
                   )))
                   .when().get("/oauth2/authorize/kakao?redirect_uri=https://druwa.netlify.com/oauth/check")
                   .then()
                   .assertThat()
                   .header(HttpHeaders.LOCATION, Matchers.matchesRegex(regex))
                   .statusCode(is(HttpStatus.FOUND.value()));
    }
}