package me.druwa.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama.model.Drama;

@Configuration
@RequiredArgsConstructor
public class ValidatorConfig {
    private final Validator defaultValidator;

    @Bean
    public DramaMultipartRequestValidator dramaMultipartRequestValidator() {
        return new DramaMultipartRequestValidator(defaultValidator);
    }

    @RequiredArgsConstructor
    public static class DramaMultipartRequestValidator implements Validator {
        private final Validator defaultValidator;

        @Override
        public boolean supports(Class<?> clazz) {
            return Drama.View.Create.MultipartRequest.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            Drama.View.Create.MultipartRequest fileModel = (Drama.View.Create.MultipartRequest) target;

            if (fileModel.getImage() != null && fileModel.getImage().isEmpty()) {
                errors.rejectValue("file", "file.empty");
            }
            defaultValidator.validate(fileModel, errors);
        }
    }
}
