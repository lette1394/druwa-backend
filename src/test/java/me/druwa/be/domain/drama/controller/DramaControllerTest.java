package me.druwa.be.domain.drama.controller;

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
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.util.AutoSpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.settings;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@Rollback
@AutoSpringBootTest
@ActiveProfiles("test")
class DramaControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    private static final String documentKey = "drama";

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void create() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document(documentKey,
                                    requestFields(
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("productionCompany").description("제작사")
                                                                              .type(JsonFieldType.STRING)
                                                                              .attributes(request.constraint(
                                                                                      "productionCompany")),
                                            fieldWithPath("summary").description("")
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(request.constraint("summary"))),
                                    responseFields(
                                            fieldWithPath("dramaId").type(JsonFieldType.NUMBER)
                                                                    .description("created id of drama entity"))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "  \"title\": \"연애플레이리스트 시즌 1\",\n" +
                                 "  \"summary\": \"새학기 시작을 풋풋한 대학 청춘 멜로와 함께\",\n" +
                                 "  \"productionCompany\": \"플레이리스트\"\n" +
                                 "}"
                   )
                   .when().post("/dramas")
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_create_response.json"))
                   .statusCode(is(HttpStatus.CREATED.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void find() {
    }

    @Test
    void update() {
    }

    @Test
    void like() {
    }

    @Test
    void dislike() {
    }
}