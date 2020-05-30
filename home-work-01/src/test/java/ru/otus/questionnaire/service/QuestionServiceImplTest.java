package ru.otus.questionnaire.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.QuestionType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("Тестирование QuestionService")
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionDao questionDao;

    @DisplayName("Получть список вопросов")
    @Test
    void getAll() {
        final Question question = Question.builder()
                .type(QuestionType.VARIANT)
                .text("questionText")
                .answerList(Arrays.asList(new Answer("answer1"), new Answer("answer2")))
                .build();

        Mockito.when(questionDao.findAll())
                .thenReturn(Collections.singletonList(question));

        QuestionService questionService = new QuestionServiceImpl(questionDao);
        final List<Question> questionList = questionService.getAll();

        Assertions.assertThat(questionList)
                .isNotEmpty()
                .contains(question);
    }
}