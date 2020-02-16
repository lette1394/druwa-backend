package me.druwa.be.domain.drama_episode_comment.controller;

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
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;
import me.druwa.be.util.AutoSpringBootTest;

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
class DramaEpisodeCommentControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void create() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__create",
                                    requestFields(
                                            fieldWithPath("depth").description(
                                                    "indent for comment. representing recursive sub-comment. default is 0 (no depth)")
                                                                  .type(JsonFieldType.NUMBER)
                                                                  .optional()
                                                                  .attributes(request.constraint("depth")),
                                            fieldWithPath("contents").description("body for comment")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("contents"))),
                                    responseFields(
                                            fieldWithPath("id").description("created comment's id")
                                                               .type(JsonFieldType.NUMBER),
                                            fieldWithPath("createdAt").description("create time of comment")
                                                                      .type(JsonFieldType.STRING),
                                            fieldWithPath("prev").description("previous comment's id")
                                                                 .type(JsonFieldType.NUMBER),
                                            fieldWithPath("next").description("next comment's id")
                                                                 .type(JsonFieldType.NUMBER))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "\t\"depth\": 1,\n" +
                                 "\t\"contents\": \"hello world!!\"\n" +
                                 "}")
                   .when().post("/dramas/{dramaId}/episodes/{episodeId}/comments", 15, 128)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.CREATED.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void append() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__append",
                                    requestFields(
                                            fieldWithPath("depth").description(
                                                    "indent for comment. representing recursive sub-comment. default is 1 (one depth)")
                                                                  .type(JsonFieldType.NUMBER)
                                                                  .optional()
                                                                  .attributes(request.constraint("depth")),
                                            fieldWithPath("contents").description("body for comment")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("contents")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "\t\"depth\": 1,\n" +
                                 "\t\"contents\": \"append comments\"\n" +
                                 "}")
                   .when().post("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}", 15, 128, 119)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.CREATED.value()));
    }

    @Test
    void edit() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__edit",
                                    requestFields(
                                            fieldWithPath("contents").description("body for comment")
                                                                     .type(JsonFieldType.STRING)
                                                                     .attributes(request.constraint("contents")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "\t\"contents\": \"서강준 갑자기 노래부르고 쓰러질 때 뿜었다진짜\"\n" +
                                 "}")
                   .when().patch("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}", 15, 128, 119)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    void list() {
        given(spec).that()
                   .filter(document("drama-episode-comment__list",
                                    responseFields(
                                            fieldWithPath("[]").description("")
                                                               .type(JsonFieldType.ARRAY),
                                            fieldWithPath("[].id").description("")
                                                                  .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[].depth").description("")
                                                                     .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[].isRoot").description("")
                                                                    .type(JsonFieldType.BOOLEAN),
                                            fieldWithPath("[].prev").description("")
                                                                    .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[].contents").description("")
                                                                        .type(JsonFieldType.STRING),
                                            fieldWithPath("[].like").description("")
                                                                    .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[].liked").description("")
                                                                    .type(JsonFieldType.BOOLEAN),
                                            fieldWithPath("[].dislike").description("")
                                                                       .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[].disliked").description("")
                                                                       .type(JsonFieldType.BOOLEAN),
                                            fieldWithPath("[].createdAt").description("create time of comment")
                                                                         .type(JsonFieldType.STRING),
                                            fieldWithPath("[].updatedAt").description("last update time of comment")
                                                                         .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/episodes/{episodeId}/comments", 1, 455)
                   .then().assertThat()
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void like() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Like.Response.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__like",
                                    responseFields(
                                            fieldWithPath("like").description("count of comment like")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(response.constraint("like")),
                                            fieldWithPath("liked").description("count of comment like")
                                                                 .type(JsonFieldType.BOOLEAN)
                                                                 .attributes(response.constraint("liked")),
                                            fieldWithPath("dislike").description("count of comment dislike")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(response.constraint("dislike")),
                                            fieldWithPath("disliked").description("count of comment like")
                                                                  .type(JsonFieldType.BOOLEAN)
                                                                  .attributes(response.constraint("disliked")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().patch("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/like", 15, 128, 80)
                   .then().assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_patch_like.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void dislike() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Like.Response.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__dislike",
                                    responseFields(
                                            fieldWithPath("like").description("count of comment like")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(response.constraint("like")),
                                            fieldWithPath("liked").description("count of comment like")
                                                                  .type(JsonFieldType.BOOLEAN)
                                                                  .attributes(response.constraint("liked")),
                                            fieldWithPath("dislike").description("count of comment dislike")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(response.constraint("dislike")),
                                            fieldWithPath("disliked").description("count of comment dislike")
                                                                    .type(JsonFieldType.BOOLEAN)
                                                                    .attributes(response.constraint("disliked")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().patch("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/dislike", 15, 128, 80)
                   .then().assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_patch_like.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}