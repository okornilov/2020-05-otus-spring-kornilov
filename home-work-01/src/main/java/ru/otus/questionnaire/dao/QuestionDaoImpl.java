package ru.otus.questionnaire.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.domain.QuestionType;
import ru.otus.questionnaire.util.CSVReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {

    private final static String SPLITTER = ";";
    private final CSVReader csvReader;

    public List<Question> findAll() {
        final List<String> stringList = csvReader.load();
        return stringList.stream()
                .map(this::parseLine)
                .collect(Collectors.toList());
    }

    private Question parseLine(String line) {
        final String[] lineStrings = line.split(SPLITTER);
        List<Answer> answerList = Arrays.stream(lineStrings)
                .skip(3)
                .map(Answer::new)
                .collect(Collectors.toList());

        return Question.builder()
                .text(lineStrings[0])
                .type(QuestionType.valueOf(Integer.parseInt(lineStrings[1])))
                .answerList(answerList)
                .build();
    }
}
