package ru.otus.questionnaire.dao;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.questionnaire.config.TestConfig;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.mettaannotaions.TestProfile;
import ru.otus.questionnaire.util.CSVReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование QuestionDao")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = {QuestionDaoImpl.class, CSVReader.class, TestConfig.class})
@TestProfile
class QuestionDaoImplTest {

    @Autowired
    private QuestionDao questionDao;

    @DisplayName("Получить все вопросы")
    @Test
    void findAllTest() {
        final List<Question> questionList = questionDao.findAll();

        assertThat(questionList)
                .asList()
                .hasSize(1)
                .asInstanceOf(InstanceOfAssertFactories.list(Question.class))
                .element(0)
                .extracting(Question::getText)
                .isEqualTo("Test question");
    }
}