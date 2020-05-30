package ru.otus.questionnaire.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum QuestionType {
    VARIANT(0),
    FREE(1);

    private final int type;

    public static QuestionType valueOf(int type) {
        return Arrays.stream(values())
                .filter(questionType -> questionType.type == type)
                .findFirst()
                .orElse(FREE);
    }
}
