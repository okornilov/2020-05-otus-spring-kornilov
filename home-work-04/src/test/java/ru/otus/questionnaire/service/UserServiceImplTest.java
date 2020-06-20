package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.questionnaire.TestApplication;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование UserServiceImpl")
@SpringBootTest(classes = TestApplication.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private IOService ioService;

    @MockBean
    private AuthenticationService authenticationService;

    @DisplayName("Задать вопрос")
    @Test
    void askQuestion() {
        Question testQuestion = Question.builder()
                .text("Test question")
                .correctAnswer(2)
                .answerList(Collections.singletonList(new Answer("Answer1")))
                .build();

        Mockito.when(ioService.read())
                .thenReturn(testQuestion.getCorrectAnswer().toString());


        int answer  = userService.askQuestion(testQuestion);

        assertThat(answer).isEqualTo(testQuestion.getCorrectAnswer());
        verify(ioService).print(testQuestion.getText());
    }

    @DisplayName("Вывести результат тестирования")
    @Test
    void showInterviewResult() {

        when(authenticationService.getUser())
                .thenReturn(new User("Ivan"));

        userService.showInterviewResult(InterviewDetail.builder()
                .passed(true)
                .totalQuestions(10)
                .correctAnswers(8)
                .build());

        verify(ioService).print("Ivan, current interview passed");
        verify(ioService).print("Number of correct answers 8 of 10");
    }
}