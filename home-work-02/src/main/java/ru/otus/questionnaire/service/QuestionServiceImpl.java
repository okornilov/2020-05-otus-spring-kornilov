package ru.otus.questionnaire.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.exception.CSVParseException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final IOService ioService;
    private final MessageBundleService messageBundleService;
    private final float reqCorrectAnswerPercent;


    public QuestionServiceImpl(QuestionDao questionDao, IOService ioService, MessageBundleService messageBundleService,
                               @Value("${questions.required-correct-answer-percent}") float reqCorrectAnswerPercent) {
        this.questionDao = questionDao;
        this.ioService = ioService;
        this.messageBundleService = messageBundleService;
        this.reqCorrectAnswerPercent = reqCorrectAnswerPercent;
    }

    @Override
    public void makeInterview() {
        List<Question> questionList = null;
        try {
            questionList = questionDao.findAll();
        } catch (CSVParseException e) {
            ioService.print(messageBundleService.getMessage("csv-parse-error"));
            return;
        }

        ioService.print(messageBundleService.getMessage("what-is-your-name"));
        final String name = ioService.read();
        int positiveCount = 0;

        for (Question question : questionList) {
            ioService.print(question.getText());
            final List<Answer> answerList = question.getAnswerList();
            if (answerList != null) {
                IntStream.range(0, answerList.size()).forEach(index -> {
                    final Answer answer = Optional.ofNullable(answerList.get(index)).orElse(new Answer());
                    ioService.print(String.format("\t%d. %s", index + 1,  answer.getTest()));
                });
            }
            Integer answerNum = Integer.parseInt(ioService.read());
            positiveCount += (Objects.equals(question.getCorrectAnswer(), answerNum) ? 1 : 0);
        }

        showResult(questionList, name, positiveCount);
    }

    private void showResult(List<Question> questionList, String name, int positiveCount) {
        boolean passed = (100.0 /questionList.size() * positiveCount) >= reqCorrectAnswerPercent;
        ioService.print(messageBundleService.getMessage(passed ? "interview-passed" : "interview-not-passed", name));
        ioService.print(messageBundleService.getMessage("number-result-interview", positiveCount, questionList.size()));
    }
}
