package ru.otus.questionnaire.service;

import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.User;

public interface UserService {
    User askName();
    int askQuestion(Question question);
    void showInterviewResult(InterviewDetail interviewDetail);
}
