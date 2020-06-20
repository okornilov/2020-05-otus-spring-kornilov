package ru.otus.questionnaire.service;

import ru.otus.questionnaire.domain.InterviewDetail;
import ru.otus.questionnaire.domain.Question;

public interface UserService {
    int askQuestion(Question question);
    void showInterviewResult(InterviewDetail interviewDetail);
}
