package ru.otus.questionnaire.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otus.questionnaire.service.IOService;
import ru.otus.questionnaire.service.QuestionIOService;

import java.io.PrintStream;
import java.util.Scanner;

@EnableConfigurationProperties
@Configuration
public class AppConfig {

    @Bean
    @Profile("!test")
    public IOService ioService() {
        return new QuestionIOService(new PrintStream(System.out), new Scanner(System.in));
    }

    @ConfigurationProperties(prefix = "questions")
    @Bean
    public QuestionProps questionProps() {
        return new QuestionProps();
    }
}
