package ru.otus.questionnaire.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;
import ru.otus.questionnaire.exception.CSVParseException;
import ru.otus.questionnaire.util.CSVReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final static String SPLITTER = ";";
    private final String questionsFileName;
    private final CSVReader csvReader;

    public QuestionDaoImpl(@Value("${questions.file-name}") String questionsFileName, CSVReader csvReader) {
        this.questionsFileName = questionsFileName;
        this.csvReader = csvReader;
    }

    public List<Question> findAll() {
        List<String> stringList = csvReader.load(questionsFileName);
        List<Question> questionList = new ArrayList<>(stringList.size());

        for (String line : stringList) {
            questionList.add(this.parseLine(line));
        }

        return questionList;
    }

    private Question parseLine(String line) throws CSVParseException {
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
