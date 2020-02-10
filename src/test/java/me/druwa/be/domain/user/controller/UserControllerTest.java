package me.druwa.be.domain.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.specification.RequestSpecification;
import me.druwa.be.docs.ConstraintAttribute;
import me.druwa.be.docs.DocsUtils;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.util.AutoSpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@Rollback
@AutoSpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void me() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(User.View.Read.Response.class);

        given(spec).that()
                   .filter(document("user__me",
                                    responseFields(
                                            fieldWithPath("name").description("")
                                                                 .attributes(response.constraint("name"))
                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("email").description("")
                                                                  .attributes(response.constraint("email"))
                                                                  .type(JsonFieldType.STRING),
                                            fieldWithPath("imageUrl").description("")
                                                                     .optional()
                                                                     .attributes(response.constraint("imageUrl"))
                                                                     .type(JsonFieldType.STRING),
                                            fieldWithPath("provider").description("")
                                                                     .attributes(response.constraint("provider"))
                                                                     .optional()
                                                                     .type(JsonFieldType.STRING),
                                            fieldWithPath("isEmailVerified").description("")
                                                                            .attributes(response.constraint(
                                                                                    "isEmailVerified"))
                                                                            .optional()
                                                                            .type(JsonFieldType.BOOLEAN),
                                            fieldWithPath("registeredAt").description("")
                                                                         .attributes(response.constraint("registeredAt"))
                                                                         .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/users/me")
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/users_me_get.json"))
                   .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    void signup() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(User.View.Create.Request.class);
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(User.View.Create.Response.class);

        given(spec).that()
                   .filter(document("user__signup",
                                    requestFields(
                                            fieldWithPath("name").description("can be duplicated")
                                                                 .attributes(request.constraint("name"))
                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("email").description("must be unique")
                                                                  .attributes(request.constraint("email"))
                                                                  .type(JsonFieldType.STRING),
                                            fieldWithPath("password").description("")
                                                                     .attributes(request.constraint("password"))
                                                                     .type(JsonFieldType.STRING)),
                                    responseFields(
                                            fieldWithPath("token").description("")
                                                                  .attributes(response.constraint("token"))
                                                                  .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body("{\n" +
                                 "  \"name\": \"namedruwa\",\n" +
                                 "  \"email\": \"druwa77@daum.net\",\n" +
                                 "  \"password\": \"123456aA\"\n" +
                                 "}")
                   .when().post("/users/signup")
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.CREATED.value()));
    }

    @Test
    void validateSuccess() {
        given(spec).that()
                   .filter(document("user__validate__success",
                                    requestParameters(
                                            parameterWithName("email").description("중복 검사 하려는 email"))))
                   .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                   .when()
                   .param("email", "unused_email@test.com")
                   .get("/users/signup/validate")
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    void validateFail() {
        given(spec).that()
                   .filter(document("user__validate__fail",
                                    requestParameters(
                                            parameterWithName("email").description("중복 검사 하려는 email"))))
                   .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                   .when()
                   .param("email", "druwa77@daum.net")
                   .get("/users/signup/validate")
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.CONFLICT.value()));
    }

    @Test
    void login() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(User.View.Login.Request.class);

        given(spec).that()
                   .filter(document("user__login",
                                    requestFields(
                                            fieldWithPath("email").description("")
                                                                  .attributes(request.constraint("email"))
                                                                  .type(JsonFieldType.STRING),
                                            fieldWithPath("password").description("")
                                                                     .attributes(request.constraint("password"))
                                                                     .type(JsonFieldType.STRING)),
                                    responseFields(
                                            fieldWithPath("token").description("")
                                                                  .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body("{\n" +
                                 "  \"email\": \"druwa77@daum.net\",\n" +
                                 "  \"password\": \"123456aA\"\n" +
                                 "}")
                   .when().post("/users/login")
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()));
    }

//    @Test
//    void find() {
//        final ConstraintAttribute request = ConstraintAttribute.createAttribute(User.View.Login.Request.class);
//
//        given(spec).that()
//                   .filter(document("user__login",
//                                    requestFields(
//                                            fieldWithPath("email").description("")
//                                                                  .attributes(request.constraint("email"))
//                                                                  .type(JsonFieldType.STRING),
//                                            fieldWithPath("password").description("")
//                                                                     .attributes(request.constraint("password"))
//                                                                     .type(JsonFieldType.STRING)),
//                                    responseFields(
//                                            fieldWithPath("token").description("")
//                                                                  .type(JsonFieldType.STRING))))
//                   .accept(MediaType.APPLICATION_JSON_VALUE)
//                   .when().get("/users/login")
//                   .then()
//                   .assertThat()
//                   .statusCode(is(HttpStatus.CONFLICT.value()));
//    }
}