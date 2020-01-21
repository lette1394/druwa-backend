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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
                                            fieldWithPath("registeredAt").description("")
                                                                         .attributes(response.constraint("registeredAt"))
                                                                         .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/users/me")
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/users_me_get.json"))
                   .statusCode(is(HttpStatus.OK.value()));
    }

}