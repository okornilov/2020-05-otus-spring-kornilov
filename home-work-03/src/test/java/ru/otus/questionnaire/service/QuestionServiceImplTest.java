package ru.otus.questionnaire.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.questionnaire.config.QuestionProps;
import ru.otus.questionnaire.config.TestConfig;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.mettaannotaions.TestProfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@DisplayName("Тестирование QuestionService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = {MessageBundleServiceImpl.class, TestConfig.class})
@TestProfile
class QuestionServiceImplTest {

    @Autowired
    private QuestionProps questionProps;

    @Autowired
    private MessageBundleService messageBundleService;

    @MockBean
    private QuestionDao questionDao;

    @DisplayName("Провести интервью")
    @Test
    void makeInterview() {

        when(questionDao.findAll()).thenReturn(
                Collections.singletonList(new Question("Question1", 2, answerList("Answer1", "Answer3", "Answer3"))));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        final Scanner scanner = new Scanner(new ByteArrayInputStream("Ivan\n2\n".getBytes()));
        final IOService ioService = new QuestionIOService(printStream, scanner);

        UserService userService = new UserServiceImpl(ioService, messageBundleService);
        QuestionService questionService = new QuestionServiceImpl(questionDao, ioService, userService, messageBundleService, questionProps);
        questionService.makeInterview();

        Assertions.assertThat(outputStream.toString())
                .contains("Ivan, current interview passed")
                .contains("Number of correct answers 1 of 1");
    }

    private List<Answer> answerList(String ... answers) {
        return Arrays.stream(answers).map(Answer::new).collect(Collectors.toList());
    }
}