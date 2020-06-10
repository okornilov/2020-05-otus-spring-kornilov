package ru.otus.questionnaire.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.questionnaire.config.QuestionProps;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.User;
import ru.otus.questionnaire.exception.CSVParseException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final IOService ioService;
    private final UserService userService;
    private final MessageBundleService messageBundleService;
    private final QuestionProps questionProps;

    @Override
    public void makeInterview() {
        List<Question> questionList;
        try {
            questionList = questionDao.findAll();
        } catch (CSVParseException e) {
            ioService.print(messageBundleService.getMessage("csv-parse-error"));
            log.error(e.getMessage(), e);
            return;
        }

        User currentUser = userService.askName();
        int positiveCount = 0;

        for (Question question : questionList) {
            int answerNum = userService.askQuestion(question);
            positiveCount += (Objects.equals(question.getCorrectAnswer(), answerNum) ? 1 : 0);
        }

        userService.showInterviewResult(InterviewDetail.builder()
                .user(currentUser)
                .correctAnswers(positiveCount)
                .totalQuestions(questionList.size())
                .passed(isPassed(questionList, positiveCount))
                .build());
    }

    private boolean isPassed(List<Question> questionList, int positiveCount) {
        return (100.0 /questionList.size() * positiveCount) >=
                Optional.ofNullable(questionProps.getRequiredCorrectAnswerPercent()).orElse(100f);
    }

}
