package me.druwa.be.domain.drama_tag;

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
import me.druwa.be.util.AutoSpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@AutoSpringBootTest
@ActiveProfiles("test")
class DramaTagControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        spec = DocsUtils.requestSpecification(restDocumentation, port);
    }

    @Test
    void find() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaTags.View.Response.class);

        given(spec).that()
                   .filter(document("drama-tag__find",
                                    requestParameters(
                                            parameterWithName("q").description("search texts. The delimiter is \",\"")),
                                    responseFields(
                                            fieldWithPath("tags").description("")
                                                                 .attributes(response.constraint("tags"))
                                                                 .type(JsonFieldType.ARRAY))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .queryParam("q", "오피스,힐링")
                   .when().get("/tags")
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_tag_find_get.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void findAll() {
        final ConstraintAttribute response = ConstraintAttribute.createAttribute(DramaTags.View.Response.class);

        given(spec).that()
                   .filter(document("drama-tag__find__all",
                                    responseFields(
                                            fieldWithPath("tags").description("")
                                                                 .attributes(response.constraint("tags"))
                                                                 .type(JsonFieldType.ARRAY))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .header(DocsUtils.testAuthorization())
                   .when().get("/tags")
                   .then()
                   .assertThat()
                   .body(matchesJsonSchemaInClasspath("json/schema/drama_tag_find_get.json"))
                   .statusCode(is(HttpStatus.OK.value()))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}