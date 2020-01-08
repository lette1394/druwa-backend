package me.druwa.be.docs;

import org.springframework.restdocs.RestDocumentationContextProvider;
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
        RequestSpecBuilder spec = new RequestSpecBuilder();
        return spec.addFilter(documentationConfiguration(restDocumentation)
                                      .operationPreprocessors()
                                      .withRequestDefaults(modifyUris()
                                                                   .host("ec2-54-180-32-97.ap-northeast-2.compute.amazonaws.com")
                                                                   .port(8080))
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

    public static Header testAuthorization() {
        return new Header("Authorization", String.format("Bearer %s", TestUtils.testToken()));
    }
}
