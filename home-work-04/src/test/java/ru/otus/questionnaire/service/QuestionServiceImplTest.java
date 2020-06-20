package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.questionnaire.TestApplication;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование QuestionService")
@SpringBootTest(classes = TestApplication.class)
class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private UserService userService;

    @DisplayName("Провести интервью")
    @Test
    void startInterview() {
        Question question = new Question("Question1", 2, answerList("Answer1", "Answer3", "Answer3"));

        when(questionDao.findAll()).thenReturn(
                Collections.singletonList(question));
        when(userService.askQuestion(eq(question)))
                .thenReturn(2);

        questionService.startInterview();

        verify(userService).askQuestion(eq(question));
        verify(userService).showInterviewResult(eq(InterviewDetail.builder()
                .correctAnswers(1)
                .totalQuestions(1)
                .passed(true)
                .build()));
    }

    private List<Answer> answerList(String ... answers) {
        return Arrays.stream(answers).map(Answer::new).collect(Collectors.toList());
    }
}