package ru.otus.questionnaire.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@Import(AppConfig.class)
public class TestConfig {
    @Bean
    public MessageSource messageSource () {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages-test");
        return messageSource;
    }
}
