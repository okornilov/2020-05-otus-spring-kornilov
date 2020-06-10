package ru.otus.questionnaire.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionProps {
    private String fileName;
    private Float requiredCorrectAnswerPercent;
}
