package ru.otus.questionnaire.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.util.CSVReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование QuestionDao")
class QuestionDaoImplTest {

    @DisplayName("Получить все вопросы")
    @Test
    void findAllTest() {
        final QuestionDao questionDao = new QuestionDaoImpl("/questions-test.csv", new CSVReader());
        final List<Question> questionList = questionDao.findAll();

        assertThat(questionList).isNotNull().isNotEmpty();
    }
}