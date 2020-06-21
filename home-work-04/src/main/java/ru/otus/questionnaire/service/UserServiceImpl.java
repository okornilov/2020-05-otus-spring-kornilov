package ru.otus.questionnaire.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final IOService ioService;
    private final AuthenticationService authenticationService;
    private final MessageBundleService messageBundleService;

    @Override
    public int askQuestion(@NonNull Question question) {
        ioService.print(question.getText());
        final List<Answer> answerList = question.getAnswerList();
        if (answerList != null) {
            IntStream.range(0, answerList.size()).forEach(index -> {
                final Answer answer = Optional.ofNullable(answerList.get(index)).orElse(new Answer());
                ioService.print(String.format("\t%d. %s", index + 1,  answer.getTest()));
            });
        }
        return Integer.parseInt(ioService.read());
    }

    @Override
    public void showInterviewResult(@NonNull InterviewDetail interviewDetail) {
        User user = authenticationService.getUser();
        ioService.print(messageBundleService.getMessage(interviewDetail.isPassed() ? "interview-passed" : "interview-not-passed", user.getName()));
        ioService.print(messageBundleService.getMessage("number-result-interview", interviewDetail.getCorrectAnswers(), interviewDetail.getTotalQuestions()));
    }

}
