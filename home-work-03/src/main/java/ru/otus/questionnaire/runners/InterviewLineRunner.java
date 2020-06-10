package ru.otus.questionnaire.runners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.questionnaire.service.QuestionService;

@Slf4j
@RequiredArgsConstructor
@Component
public class InterviewLineRunner implements CommandLineRunner {

    private final QuestionService questionService;

    @Override
    public void run(String... args) {
        questionService.makeInterview();
    }
}
