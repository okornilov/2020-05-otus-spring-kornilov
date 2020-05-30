package ru.otus.questionnaire.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.util.CSVReader;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@DisplayName("Тестирование QuestionDao")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    @Mock
    private CSVReader csvReader;

    @DisplayName("Получить все вопросы")
    @Test
    void findAllTest() {
        when(csvReader.load())
                .thenReturn(Collections.singletonList("question;0;1;answer1;answer2;answer3;answer4"));

        final QuestionDao questionDao = new QuestionDaoImpl(csvReader);
        final List<Question> questionList = questionDao.findAll();

        Assertions.assertThat(questionList).isNotNull().isNotEmpty();
    }
}