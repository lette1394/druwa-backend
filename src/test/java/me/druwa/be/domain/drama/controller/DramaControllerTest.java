package me.druwa.be.domain.drama.controller;

import java.io.File;

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
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.util.AutoSpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@Rollback
@AutoSpringBootTest
@ActiveProfiles("test")
class DramaControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void create() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__create",
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
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_post.json"))
                   .statusCode(is(HttpStatus.CREATED.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void find() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__find",
                                    responseFields(
                                            fieldWithPath("dramaId").description("")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(request.constraint("dramaId")),
                                            fieldWithPath("title").description("")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("productionCompany").description("")
                                                                              .type(JsonFieldType.STRING)
                                                                              .attributes(request.constraint(
                                                                                      "productionCompany")),
                                            fieldWithPath("images").description("")
                                                                   .optional()
                                                                   .type(JsonFieldType.ARRAY)
                                                                   .attributes(request.constraint("images")),
                                            fieldWithPath("like").description("")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(request.constraint("like")),
                                            fieldWithPath("liked").description("")
                                                                 .type(JsonFieldType.BOOLEAN)
                                                                 .attributes(request.constraint("liked")),
                                            fieldWithPath("dislike").description("")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(request.constraint("dislike")),
                                            fieldWithPath("disliked").description("")
                                                                    .type(JsonFieldType.BOOLEAN)
                                                                    .attributes(request.constraint("disliked")),
                                            fieldWithPath("createdAt").description("")
                                                                      .type(JsonFieldType.STRING)
                                                                      .attributes(request.constraint("createdAt")),
                                            fieldWithPath("updatedAt").description("")
                                                                      .type(JsonFieldType.STRING)
                                                                      .attributes(request.constraint("updatedAt")),
                                            fieldWithPath("summary").description("")
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(request.constraint("summary")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_id_get.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void related() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__related",
                                    responseFields(
                                            fieldWithPath("[]").description("")
                                                               .type(JsonFieldType.ARRAY),
                                            fieldWithPath("[]dramaId").description("")
                                                                      .type(JsonFieldType.NUMBER)
                                                                      .attributes(request.constraint("dramaId")),
                                            fieldWithPath("[]title").description("")
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(request.constraint("title")),
                                            fieldWithPath("[]productionCompany").description("")
                                                                                .type(JsonFieldType.STRING)
                                                                                .attributes(request.constraint(
                                                                                        "productionCompany")),
                                            fieldWithPath("[]images").description("")
                                                                     .optional()
                                                                     .type(JsonFieldType.ARRAY)
                                                                     .attributes(request.constraint("images")),
                                            fieldWithPath("[]images.[]imageName").description("")
                                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("[]images.[]imageUrl").description("")
                                                                                .type(JsonFieldType.STRING),
                                            fieldWithPath("[]images.[]widthPixel").description("")
                                                                                  .optional()
                                                                                  .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[]images.[]heightPixel").description("")
                                                                                   .optional()
                                                                                   .type(JsonFieldType.NUMBER),
                                            fieldWithPath("[]images.[]imageType").description("")
                                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("[]images.[]createdAt").description("")
                                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("[]images.[]updatedAt").description("")
                                                                                 .type(JsonFieldType.STRING),
                                            fieldWithPath("[]like").description("")
                                                                   .type(JsonFieldType.NUMBER)
                                                                   .attributes(request.constraint("like")),
                                            fieldWithPath("[]liked").description("")
                                                                   .type(JsonFieldType.BOOLEAN)
                                                                   .attributes(request.constraint("liked")),
                                            fieldWithPath("[]dislike").description("")
                                                                      .type(JsonFieldType.NUMBER)
                                                                      .attributes(request.constraint("dislike")),
                                            fieldWithPath("[]disliked").description("")
                                                                      .type(JsonFieldType.BOOLEAN)
                                                                      .attributes(request.constraint("disliked")),
                                            fieldWithPath("[]createdAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(request.constraint("createdAt")),
                                            fieldWithPath("[]updatedAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(request.constraint("updatedAt")),
                                            fieldWithPath("[]summary").description("")
                                                                      .type(JsonFieldType.STRING)
                                                                      .attributes(request.constraint("summary")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/related", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_id_get.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void update() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__update",
                                    requestFields(
                                            fieldWithPath("title").description("")
                                                                  .optional()
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(request.constraint("title")),
                                            fieldWithPath("productionCompany").description("")
                                                                              .optional()
                                                                              .type(JsonFieldType.STRING)
                                                                              .attributes(request.constraint(
                                                                                      "productionCompany")),
                                            fieldWithPath("summary").description("")
                                                                    .optional()
                                                                    .type(JsonFieldType.STRING)
                                                                    .attributes(request.constraint("summary")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .body("" +
                                 "{\n" +
                                 "  \"title\": \"연애플레이리스트 시즌 1\",\n" +
                                 "  \"productionCompany\": \"플레이리스트\"\n" +
                                 "}"
                   )
                   .when().patch("/dramas/{dramaId}", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_post.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void like() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__like",
                                    responseFields(
                                            fieldWithPath("like").description("")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(request.constraint("like")),
                                            fieldWithPath("liked").description("")
                                                                  .type(JsonFieldType.BOOLEAN)
                                                                  .attributes(request.constraint("liked")),
                                            fieldWithPath("dislike").description("")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(request.constraint("like")),
                                            fieldWithPath("disliked").description("")
                                                                     .type(JsonFieldType.BOOLEAN)
                                                                     .attributes(request.constraint("disliked")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().patch("/dramas/{dramaId}/like", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_id_like.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void dislike() {
        final ConstraintAttribute request = ConstraintAttribute.createAttribute(Drama.View.Create.Request.class);

        given(spec).that()
                   .filter(document("drama__dislike",
                                    responseFields(
                                            fieldWithPath("like").description("")
                                                                 .type(JsonFieldType.NUMBER)
                                                                 .attributes(request.constraint("like")),
                                            fieldWithPath("liked").description("")
                                                                  .type(JsonFieldType.BOOLEAN)
                                                                  .attributes(request.constraint("liked")),
                                            fieldWithPath("dislike").description("")
                                                                    .type(JsonFieldType.NUMBER)
                                                                    .attributes(request.constraint("like")),
                                            fieldWithPath("disliked").description("")
                                                                     .type(JsonFieldType.BOOLEAN)
                                                                     .attributes(request.constraint("disliked")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().patch("/dramas/{dramaId}/dislike", 15)
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/dramas_id_like.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void createImages() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(Image.View.Read.Response.class);

        given(spec).that()
                   .filter(document("drama__create__image",
                                    requestParts(
                                            partWithName("imageName1").description(
                                                    "The unique name and key of image in this drama. You can set any string in here. " +
                                                            "It could be overwrited and you can get the image using this key." +
                                                            "for example, 'GET /dramas/{dramaId}/images/{imageName}")
                                    ),
                                    responseFields(
                                            fieldWithPath("[]imageName").description("")
                                                                        .optional()
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("imageName")),
                                            fieldWithPath("[]imageUrl").description("")
                                                                       .optional()
                                                                       .type(JsonFieldType.STRING)
                                                                       .attributes(response.constraint(
                                                                               "imageUrl")),
                                            fieldWithPath("[]widthPixel").description("")
                                                                         .optional()
                                                                         .type(JsonFieldType.NUMBER)
                                                                         .attributes(response.constraint("widthPixel")),
                                            fieldWithPath("[]imageType").description("")
                                                                        .optional()
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("imageType")),
                                            fieldWithPath("[]heightPixel").description("")
                                                                          .optional()
                                                                          .type(JsonFieldType.NUMBER)
                                                                          .attributes(response.constraint("heightPixel")),
                                            fieldWithPath("[]createdAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("createdAt")),
                                            fieldWithPath("[]updatedAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("updatedAt")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                   .multiPart("imageName1", new File("src/test/resources/img/320x200.jpg"), MediaType.IMAGE_JPEG_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().post("/dramas/{dramaId}/images", 1)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void listImages() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(Image.View.Read.Response.class);

        given(spec).that()
                   .filter(document("drama__list__image",
                                    responseFields(
                                            fieldWithPath("[]imageName").description("")
                                                                        .optional()
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("imageName")),
                                            fieldWithPath("[]imageUrl").description("")
                                                                       .optional()
                                                                       .type(JsonFieldType.STRING)
                                                                       .attributes(response.constraint(
                                                                               "imageUrl")),
                                            fieldWithPath("[]widthPixel").description("")
                                                                         .optional()
                                                                         .type(JsonFieldType.NUMBER)
                                                                         .attributes(response.constraint("widthPixel")),
                                            fieldWithPath("[]imageType").description("")
                                                                        .optional()
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("imageType")),
                                            fieldWithPath("[]heightPixel").description("")
                                                                          .optional()
                                                                          .type(JsonFieldType.NUMBER)
                                                                          .attributes(response.constraint("heightPixel")),
                                            fieldWithPath("[]createdAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("createdAt")),
                                            fieldWithPath("[]updatedAt").description("")
                                                                        .type(JsonFieldType.STRING)
                                                                        .attributes(response.constraint("updatedAt")))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/dramas/{dramaId}/images", 1)
                   .then()
                   .assertThat()
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}