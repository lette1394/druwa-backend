package me.druwa.be.docs;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.web.util.UriComponentsBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import me.druwa.be.util.TestUtils;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

public class DocsUtils {
//    public static FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {
//        return Arrays.stream(enumTypes)
//                     .map(enumType -> fieldWithPath(enumType.getId()).description(enumType.getText()))
//                     .toArray(FieldDescriptor[]::new);
//    }

    public static RequestSpecification requestSpecification(final RestDocumentationContextProvider restDocumentation,
                                                            final int port) {

        OperationPreprocessor noUrlEncoded = new OperationPreprocessor() {
            @Override
            public OperationRequest preprocess(final OperationRequest request) {

                return new OperationRequestFactory().create(request.getUri(),
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

        return spec.addFilter(documentationConfiguration(restDocumentation)
                                      .operationPreprocessors()
                                      .withRequestDefaults(
                                              noUrlEncoded,
                                              prettyPrint(),
                                              removeHeaders("Host"),
                                              modifyUris()
                                                      .host("api.druwa.site")
                                                      .scheme("https")
                                                      .removePort())
                                      .withResponseDefaults(
                                              prettyPrint(),
                                              removeHeaders("Date",
                                                            "Vary",
                                                            "Set-Cookie",
                                                            "druwa-debug-token",
                                                            "Authorization",
                                                            "Host",
                                                            "Keep-Alive",
                                                            "Connection",
                                                            "Transfer-Encoding"))).build()
                   .config(RestAssuredConfig.config()
                                            .httpClient(HttpClientConfig.httpClientConfig()
                                                                        .dontReuseHttpClientInstance()))
                   .port(port);
    }

    public static Header testAuthorization() {
        return new Header("Authorization", String.format("Bearer %s", TestUtils.testToken()));
    }
}
