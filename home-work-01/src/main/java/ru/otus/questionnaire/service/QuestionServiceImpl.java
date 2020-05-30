package ru.otus.questionnaire.service;

import lombok.RequiredArgsConstructor;
import ru.otus.questionnaire.dao.QuestionDao;
import ru.otus.questionnaire.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    public List<Question> getAll() {
        return questionDao.findAll();
    }
}
