package ru.otus.questionnaire.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.service.CSVService;

import java.util.Collections;
import java.util.List;

@DisplayName("Тестирование QuestionDao")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    @Mock
    private CSVService csvService;

    @DisplayName("Получить все вопросы")
    @Test
    void findAllTest() {
        Mockito.when(csvService.load())
                .thenReturn(Collections.singletonList("question;0;1;answer1;answer2;answer3;answer4"));

        QuestionDao questionDao = new QuestionDaoImpl(csvService);
        final List<Question> questionList = questionDao.findAll();

        Assertions.assertThat(questionList).isNotNull().isNotEmpty();
    }
}