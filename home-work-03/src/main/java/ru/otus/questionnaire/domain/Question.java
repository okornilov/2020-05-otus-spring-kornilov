package ru.otus.questionnaire.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question {
    private String text;
    private Integer correctAnswer;
    private List<Answer> answerList;
}
