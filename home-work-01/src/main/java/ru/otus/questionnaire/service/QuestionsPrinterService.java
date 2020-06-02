package ru.otus.questionnaire.service;

import lombok.RequiredArgsConstructor;
import ru.otus.questionnaire.domain.Answer;
import ru.otus.questionnaire.domain.Question;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class QuestionsPrinterService implements IOService {

    private final QuestionService questionService;

    @Override
    public void print(OutputStream outputStream) {
        final List<Question> questionList = questionService.getAll();
        PrintWriter writer = new PrintWriter(outputStream);
        questionList.forEach(question -> {
            writer.println(question.getText());
            final List<Answer> answerList = question.getAnswerList();
            if (answerList != null) {
                IntStream.range(0, answerList.size()).forEach(index -> {
                    final Answer answer = Optional.ofNullable(answerList.get(index)).orElse(new Answer());
                    writer.printf("\t%d. %s", index + 1,  answer.getTest());
                    writer.println();
                });
            }
            writer.println();
        });

        writer.flush();
        writer.close();
    }
}
