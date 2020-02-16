package me.druwa.be.domain.drama_review;

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
import org.apache.commons.lang3.StringUtils;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@Rollback
@AutoSpringBootTest
@ActiveProfiles("test")
class DramaReviewControllerTest {
    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void create() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(DramaReview.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-review__create",
                                    requestFields(
                                            fieldWithPath("point").description("Point should between 0 ~ 5")
                                                                  .type(JsonFieldType.NUMBER)
                                                                  .attributes(request.constraint("point")),
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("contents").description("")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("contents")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "  \"point\": 4,\n" +
                                 "  \"title\": \"서강준 미친 외모에 한 번 놀라고, 스토리에 한 번 놀라고\",\n" +
                                 "  \"contents\": \"서강준 하드캐리 원맨쇼를 원한다면 강추!\"\n" +
                                 "}"
                   )
                   .when().post("/dramas/{dramaId}/reviews", 15)
                   .then()
                   .assertThat()
                   .body(is(StringUtils.EMPTY))
                   .statusCode(is(HttpStatus.CREATED.value()));
    }

    @Test
    void list() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-review__list",
                                    responseFields(
                                            fieldWithPath("[]").description("")
                                                               .type(JsonFieldType.ARRAY),
                                            fieldWithPath("[].dramaReviewId").description("")
                                                                             .type(JsonFieldType.NUMBER)
                                                                             .attributes(request.constraint(
                                                                                     "dramaReviewId")),
                                            fieldWithPath("[].point").description("")
                                                                     .type(JsonFieldType.NUMBER)
                                                                     .attributes(request.constraint("point")),
                                            fieldWithPath("[].title").description("")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("title")),
                                            fieldWithPath("[].contents").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(request.constraint("contents")),
                                            fieldWithPath("[].createdAt").description("")
                                                                         .type(JsonFieldType.STRING)
                                                                         .attributes(request.constraint("createdAt")),
                                            fieldWithPath("[].updatedAt").description("")
                                                                         .type(JsonFieldType.STRING)
                                                                         .attributes(request.constraint("updatedAt")),
                                            fieldWithPath("[].writtenByMe").description("")
                                                                           .type(JsonFieldType.BOOLEAN)
                                                                           .attributes(request.constraint("writtenByMe")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/reviews", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_review_list.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void find() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-review__find",
                                    responseFields(
                                            fieldWithPath("dramaReviewId").description("")
                                                                          .type(JsonFieldType.NUMBER)
                                                                          .attributes(request.constraint(
                                                                                  "dramaReviewId")),
                                            fieldWithPath("point").description("")
                                                                  .type(JsonFieldType.NUMBER)
                                                                  .attributes(request.constraint("point")),
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("contents").description("")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("contents")),
                                            fieldWithPath("createdAt").description("")
                                                                      .type(JsonFieldType.STRING)
                                                                      .attributes(request.constraint("createdAt")),
                                            fieldWithPath("updatedAt").description("")
                                                                      .type(JsonFieldType.STRING)
                                                                      .attributes(request.constraint("updatedAt")),
                                            fieldWithPath("writtenByMe").description("")
                                                                        .type(JsonFieldType.BOOLEAN)
                                                                        .attributes(request.constraint("writtenByMe")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/reviews/{reviewId}", 15, 292)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_review_list.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}