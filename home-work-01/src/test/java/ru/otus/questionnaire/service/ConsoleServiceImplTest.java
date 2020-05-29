package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.QuestionType;

import java.util.Collections;

@DisplayName("Тестирование ConsoleService")
@ExtendWith(MockitoExtension.class)
class ConsoleServiceImplTest {

    @Mock
    private QuestionService questionService;

    @DisplayName("Вывести список вопросов")
    @Test
    void printQuestions() {
        Mockito.when(questionService.getAll())
                .thenReturn(Collections.singletonList(Question.builder()
                        .type(QuestionType.FREE)
                        .text("My question text")
                        .build()));
        ConsoleService consoleService = new ConsoleServiceImpl(questionService);
        consoleService.printQuestions();

        Mockito.verify(questionService).getAll();
    }
}