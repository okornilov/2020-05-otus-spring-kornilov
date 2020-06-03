package ru.otus.questionnaire.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;

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
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
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
        final MessageBundleServiceImpl messageBundleService = new MessageBundleServiceImpl(getMessageSource(), "en");

        QuestionService questionService = new QuestionServiceImpl(questionDao, ioService, messageBundleService, 100);
        questionService.makeInterview();

        Assertions.assertThat(outputStream.toString())
                .contains("Ivan, current interview passed")
                .contains("Number of correct answers 1 of 1");
    }

    private ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(-1);
        messageSource.setBasenames("classpath:messages");
        return messageSource;
    }

    private List<Answer> answerList(String ... answers) {
        return Arrays.stream(answers).map(Answer::new).collect(Collectors.toList());
    }
}