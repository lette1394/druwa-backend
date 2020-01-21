package me.druwa.be.domain.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import me.druwa.be.docs.ConstraintAttribute;
import me.druwa.be.util.AutoSpringBootTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@AutoSpringBootTest
@ActiveProfiles("test")
class TestControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        OperationPreprocessor noUrlEncoded = new OperationPreprocessor() {
            @Override
            public OperationRequest preprocess(final OperationRequest request) {
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(request.getUri());
                return new OperationRequestFactory().create(uriBuilder.build(false).toUri(),
                                                            request.getMethod(),
                                                            request.getContent(),
                                                            request.getHeaders(),
                                                            request.getParameters(),
                                                            request.getParts(),
                                                            request.getCookies());
            }

            @Override
            public OperationResponse preprocess(final OperationResponse response) {
                return response;
            }
        };

        RequestSpecBuilder spec = new RequestSpecBuilder();
        this.spec = spec.addFilter(documentationConfiguration(restDocumentation)
                                           .operationPreprocessors()
                                           .withRequestDefaults(
                                                   noUrlEncoded,
                                                   prettyPrint(),
                                                   modifyUris()
                                                           .host("api.druwa.com")
                                                           .removePort())
                                           .withResponseDefaults(prettyPrint(),
                                                                 removeHeaders("Date",
                                                                               "Keep-Alive",
                                                                               "Connection",
                                                                               "Transfer-Encoding"))).build()
                        .config(RestAssuredConfig.config()
                                                 .httpClient(HttpClientConfig.httpClientConfig()
                                                                             .dontReuseHttpClientInstance()))
                        .port(port);
    }

    @Test
    void test() {
        given(spec).that()
                   .filter(document("person", responseFields(
                           fieldWithPath("name").description("The user's name").type(JsonFieldType.STRING),
                           fieldWithPath("isMarried").description("Weather married").type(JsonFieldType.BOOLEAN),
                           fieldWithPath("hobbies").description("hooooobies").type(JsonFieldType.ARRAY),
                           fieldWithPath("kids").description("kidddds").type(JsonFieldType.ARRAY),
                           fieldWithPath("age").description("The user's age"))))
                   .accept(MediaType.APPLICATION_JSON_VALUE)


                   .when().get("/test/json/{age}", 19)
                   .then()
                   .statusCode(is(200))
                   .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void customer() {
        final ConstraintAttribute customer = ConstraintAttribute.createAttribute(Customer.class);

        given(spec).that()
                   .filter(document("customer",
                                    requestFields(
                                            fieldWithPath("name").description("this is name")
                                                                 .type(JsonFieldType.STRING)
                                                                 .optional()
                                                                 .attributes(customer.constraint("name")),
                                            fieldWithPath("email").description("this is email")
                                                                  .type(JsonFieldType.STRING)
                                                                  .attributes(customer.constraint("email")),
                                            fieldWithPath("age").description("this is age")
                                                                .type(JsonFieldType.NUMBER),
                                            fieldWithPath("gender").description("gender")
                                                                   .type(JsonFieldType.STRING)
                                                                   .attributes(customer.constraint("gender")),

                                            fieldWithPath("birthday").description("wow")
                                                                     .type(JsonFieldType.STRING),
                                            fieldWithPath("phone").description("wow")
                                                                  .type(JsonFieldType.STRING)
                                    )))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)

                   .body("{\n" +
                                 "\t\"age\": 18,\n" +
                                 "\t\"name\": \"hi\",\n" +
                                 "\t\"email\": \"lette193@naver.com\",\n" +
                                 "\t\"gender\": \"MALE\",\n" +
                                 "\t\"phone\": \"010-090-1111\",\n" +
                                 "\t\"birthday\": \"2019-12-11\"\n" +
                                 "}")
                   .when().post("/test/customer")
                   .then()
                   .statusCode(is(200));
    }
}