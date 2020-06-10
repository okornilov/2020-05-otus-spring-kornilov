package ru.otus.questionnaire.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.questionnaire.config.QuestionProps;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.exception.CSVParseException;
import ru.otus.questionnaire.util.CSVReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final static String SPLITTER = ";";
    private final QuestionProps questionProps;
    private final CSVReader csvReader;

    public List<Question> findAll() {
        List<String> stringList = csvReader.load(questionProps.getFileName());
        List<Question> questionList = new ArrayList<>(stringList.size());

        for (String line : stringList) {
            questionList.add(this.parseLine(line));
        }

        return questionList;
    }

    private Question parseLine(String line) {
        final String[] lineStrings = line.split(SPLITTER);
        if (lineStrings.length < 3) {
            throw new CSVParseException();
        }
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
