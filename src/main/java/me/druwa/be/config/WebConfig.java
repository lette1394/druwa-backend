package me.druwa.be.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import me.druwa.be.domain.common.converter.MultipartFilesConverter;
import me.druwa.be.domain.common.converter.StringToDramaSearchStringsConverter;
import me.druwa.be.domain.common.converter.StringToDramaTagSearchStringsConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/");
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/docs")
                .setViewName("forward:/docs/index.html");
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new MultipartFilesConverter());
        registry.addConverter(new StringToDramaTagSearchStringsConverter());
        registry.addConverter(new StringToDramaSearchStringsConverter());

        registry.addFormatter(new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(final String text, final Locale locale) {
                return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
            }

            @Override
            public String print(final LocalDateTime object, final Locale locale) {
                return DateTimeFormatter.ISO_DATE.format(object);
            }
        });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
