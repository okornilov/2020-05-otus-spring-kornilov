package ru.otus.questionnaire.service;

import lombok.RequiredArgsConstructor;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ConsoleServiceImpl implements ConsoleService {

    private final QuestionService questionService;

    @Override
    public void printQuestions() {
        final List<Question> questionList = questionService.getAll();
        questionList.forEach(this::printQuestion);
    }

    private void printQuestion(Question question) {
        System.out.println(question.getText());
        final List<Answer> answerList = question.getAnswerList();
        if (answerList != null) {
            IntStream.range(0, answerList.size()).forEach(index -> {
                final Answer answer = Optional.ofNullable(answerList.get(index)).orElse(new Answer());
                System.out.printf("\t%d. %s", index + 1,  answer.getTest());
                System.out.println();
            });
        }
        System.out.println();
    }
}
