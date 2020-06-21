package ru.otus.questionnaire.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "questions")
public class QuestionProps {
    private String fileName;
    private Float requiredCorrectAnswerPercent;
}
