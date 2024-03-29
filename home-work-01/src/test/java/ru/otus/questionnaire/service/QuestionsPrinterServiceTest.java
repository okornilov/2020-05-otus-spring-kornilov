package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.QuestionType;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование QuestionsPrinterService")
@ExtendWith(MockitoExtension.class)
class QuestionsPrinterServiceTest {

    @Mock
    private QuestionService questionService;

    @DisplayName("Вывести список вопросов")
    @Test
    void print() {
        Mockito.when(questionService.getAll())
                .thenReturn(Collections.singletonList(Question.builder()
                        .type(QuestionType.FREE)
                        .text("My question text")
                        .build()));

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOService printerService = new QuestionsPrinterService(questionService);
        printerService.print(byteArrayOutputStream);

        assertThat(byteArrayOutputStream.toString()).contains("My question text");
    }
}