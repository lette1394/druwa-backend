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
                                                                      .type(JsonFieldType.STRING))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "\t\"depth\": 1,\n" +
                                 "\t\"contents\": \"hello world!!\"\n" +
                                 "}")
                   .when().post("/dramas/{dramaId}/episodes/{episodeId}/comments", 15, 1)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.CREATED.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

//    @Test
//    void list() {
//        given(spec).that()
//                   .filter(document("drama-episode-comment__list",
//                                    responseFields(
//                                            fieldWithPath("[]").description("")
//                                                               .type(JsonFieldType.ARRAY),
//                                            fieldWithPath("[].id").description("comment's id")
//                                                                  .type(JsonFieldType.NUMBER),
//                                            fieldWithPath("[].depth").description("comment's id")
//                                                                     .type(JsonFieldType.NUMBER),
//                                            fieldWithPath("[].next").description("comment's id")
//                                                                    .type(JsonFieldType.NUMBER),
//                                            fieldWithPath("[].prev").description("comment's id")
//                                                                    .type(JsonFieldType.NUMBER),
//                                            fieldWithPath("[].contents").description("comment's id")
//                                                                        .type(JsonFieldType.STRING),
//                                            fieldWithPath("[].like").description("comment's id")
//                                                                    .type(JsonFieldType.NUMBER),
//                                            fieldWithPath("[].createdAt").description("create time of comment")
//                                                                         .type(JsonFieldType.STRING),
//                                            fieldWithPath("[].updatedAt").description("comment's id")
//                                                                         .type(JsonFieldType.STRING))))
//                   .accept(MediaType.APPLICATION_JSON_VALUE)
//                   .contentType(MediaType.APPLICATION_JSON_VALUE)
//                   .header(DocsUtils.testAuthorization())
//                   .when().get("/dramas/{dramaId}/episodes/{episodeId}/comments", 15, 1)
//                   .then().assertThat()
//                   .statusCode(is(HttpStatus.OK.value()))
//                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_id_get_comment.json"))
//                   .contentType(MediaType.APPLICATION_JSON_VALUE);
//    }

    @Test
    void like() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaEpisodeComment.View.Like.Response.class);

        given(spec).that()
                   .filter(document("drama-episode-comment__like",
                                    responseFields(
                                            fieldWithPath("like").description("count of comment like")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(response.constraint("like")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().post("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/like", 15, 1, 19)
                   .then().assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_post_like.json"))
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
                                                                 .attributes(response.constraint("like")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().post("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/dislike", 15, 1, 19)
                   .then().assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_post_like.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}