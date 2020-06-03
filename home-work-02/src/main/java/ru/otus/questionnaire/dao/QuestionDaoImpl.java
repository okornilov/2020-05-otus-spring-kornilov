package ru.otus.questionnaire.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.util.CSVReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionDaoImpl implements QuestionDao {

    private final static String SPLITTER = ";";
    private final String questionsFileName;

    public QuestionDaoImpl(@Value("${questions.file-name}") String questionsFileName) {
        this.questionsFileName = questionsFileName;
    }

    public List<Question> findAll() {
        final List<String> stringList = CSVReader.load(questionsFileName);
        return stringList.stream()
                .map(this::parseLine)
                .collect(Collectors.toList());
    }

    private Question parseLine(String line) {
        final String[] lineStrings = line.split(SPLITTER);
        List<Answer> answerList = Arrays.stream(lineStrings)
                .skip(2)
                .map(Answer::new)
                .collect(Collectors.toList());

        return Question.builder()
                .text(lineStrings[0])
                .correctAnswer(Integer.parseInt(lineStrings[1]))
                .answerList(answerList)
                .build();
    }
}
