package ru.otus.questionnaire.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterviewDetail {
    private User user;
    private boolean passed;
    private int correctAnswers;
    private int totalQuestions;
}
