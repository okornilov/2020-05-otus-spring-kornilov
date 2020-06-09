package ru.otus.questionnaire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.questionnaire.service.IOService;
import ru.otus.questionnaire.service.QuestionIOService;

import java.io.PrintStream;
import java.util.Scanner;

@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public IOService ioService() {
        return new QuestionIOService(new PrintStream(System.out), new Scanner(System.in));
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
}
