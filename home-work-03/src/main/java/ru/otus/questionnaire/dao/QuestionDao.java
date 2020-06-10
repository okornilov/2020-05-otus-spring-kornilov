package ru.otus.questionnaire.dao;

import ru.otus.questionnaire.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
