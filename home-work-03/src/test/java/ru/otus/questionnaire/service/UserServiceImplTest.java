package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.questionnaire.config.TestConfig;
import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.User;
import ru.otus.questionnaire.mettaannotaions.TestProfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DisplayName("Тестирование UserServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = {MessageBundleServiceImpl.class, UserServiceImpl.class, TestConfig.class})
@TestProfile
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private IOService ioService;

    @DisplayName("Спросить имя пользователя")
    @Test
    void askName() {
        Mockito.when(ioService.read())
                .thenReturn("Ivan");

        User user = userService.askName();
        assertThat(user)
                .extracting(User::getName)
                .isEqualTo("Ivan");
    }

    @DisplayName("Задать вопрос")
    @Test
    void askQuestion() {
        Question testQuestion = Question.builder()
                .text("Test question")
                .correctAnswer(2)
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
        userService.showInterviewResult(InterviewDetail.builder()
                .passed(true)
                .totalQuestions(10)
                .correctAnswers(8)
                .user(new User("Ivan"))
                .build());

        verify(ioService).print("Ivan, current interview passed");
        verify(ioService).print("Number of correct answers 8 of 10");
    }
}