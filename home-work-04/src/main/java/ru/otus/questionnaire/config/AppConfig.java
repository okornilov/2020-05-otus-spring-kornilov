package ru.otus.questionnaire.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.questionnaire.service.IOService;
import ru.otus.questionnaire.service.QuestionIOService;

import java.io.PrintStream;
import java.util.Scanner;

@EnableConfigurationProperties
@Configuration
public class AppConfig {

    @Bean
    public IOService ioService() {
        return new QuestionIOService(new PrintStream(System.out), new Scanner(System.in));
    }

    @Bean
    public QuestionProps questionProps() {
        return new QuestionProps();
    }
}
