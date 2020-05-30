package ru.otus.questionnaire;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.questionnaire.service.ConsoleService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        ConsoleService consoleService = context.getBean(ConsoleService.class);
        consoleService.printQuestions();
    }

}
