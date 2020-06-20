package ru.otus.questionnaire.dao;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.questionnaire.TestApplication;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.exception.CSVParseException;
import ru.otus.questionnaire.util.CSVReader;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование QuestionDao")
@SpringBootTest(classes = TestApplication.class)
class QuestionDaoImplTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private CSVReader csvReader;

    @DisplayName("Получить все вопросы")
    @Test
    void findAllTest() {
        when(csvReader.load(Mockito.anyString()))
                .thenReturn(Collections.singletonList("Test question;1;Answer"));

        assertThat(questionDao.findAll())
                .asList()
                .hasSize(1)
                .asInstanceOf(InstanceOfAssertFactories.list(Question.class))
                .element(0)
                .extracting(Question::getText)
                .isEqualTo("Test question");
    }

    @DisplayName("Получить все вопросы, когда файл некорректный")
    @Test
    void findAllError() {
        when(csvReader.load(Mockito.anyString()))
                .thenReturn(Collections.singletonList("Question;Answer"));

        assertThrows(CSVParseException.class, () -> questionDao.findAll());
    }

}