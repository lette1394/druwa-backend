package me.druwa.be.domain.drama_episode.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import io.restassured.specification.RequestSpecification;
import me.druwa.be.docs.ConstraintAttribute;
import me.druwa.be.docs.DocsUtils;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.util.AutoSpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@AutoSpringBootTest
class DramaEpisodeControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void create() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(DramaEpisode.View.Create.Request.class);
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaEpisode.View.Create.Response.class);

        given(spec).that()
                   .filter(document("drama-episode__create",
                                    requestFields(
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("summary").description("")
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(request.constraint("summary")),
                                            fieldWithPath("episodeNumber").description("")
                                                                          .type(JsonFieldType.NUMBER)
                                                                          .attributes(request.constraint("episodeNumber")),
                                            fieldWithPath("durationInMillis").description("")
                                                                             .type(JsonFieldType.NUMBER)
                                                                             .attributes(request.constraint(
                                                                                     "durationInMillis"))),
                                    responseFields(
                                            fieldWithPath("dramaEpisodeId").type(JsonFieldType.NUMBER)
                                                                           .attributes(response.constraint(
                                                                                   "dramaEpisodeId"))
                                                                           .description("created id of drama entity"))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "\t\"title\": \"좋아하는 사람에게 솔직해야 하는 이유\",\n" +
                                 "\t\"summary\": \"좋아하는 사람에게 솔직해야 하는 이유\\n세상 어느 고민 상담이 저렇게 꿀범벅이죠..?\uD83C\uDF6F\\n내 광대 해발고도 백두산보다 높은 순간\uD83E\uDD21\",\n" +
                                 "\t\"episodeNumber\": 15,\n" +
                                 "\t\"durationInMillis\": 911000\n" +
                                 "}")
                   .when().post("/dramas/{dramaId}/episodes", 1)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    void find() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaEpisode.View.Read.Response.class);

        given(spec).that()
                   .filter(document("drama-episode__find",
                                    responseFields(
                                            fieldWithPath("dramaEpisodeId").type(JsonFieldType.NUMBER)
                                                                           .attributes(response.constraint(
                                                                                   "dramaEpisodeId"))
                                                                           .description(
                                                                                   "created id of drama episode entity"),
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(response.constraint("title")),
                                            fieldWithPath("summary").description("")
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(response.constraint("summary")),
                                            fieldWithPath("episodeNumber").description("")
                                                                          .type(JsonFieldType.NUMBER)
                                                                          .attributes(response.constraint(
                                                                                  "episodeNumber")),
                                            fieldWithPath("like").description("")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(response.constraint(
                                                                         "like")),
                                            fieldWithPath("dislike").description("")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(response.constraint(
                                                                         "dislike")),
                                            fieldWithPath("totalComments").description("total count of comments")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(response.constraint(
                                                                            "totalComments")),
                                            fieldWithPath("durationInMillis").description("")
                                                                             .type(JsonFieldType.NUMBER)
                                                                             .attributes(response.constraint(
                                                                                     "durationInMillis")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/episodes/{episodesId}", 1, 128)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_episode_id_get.json"))
                   .statusCode(is(HttpStatus.OK.value()));
    }
}