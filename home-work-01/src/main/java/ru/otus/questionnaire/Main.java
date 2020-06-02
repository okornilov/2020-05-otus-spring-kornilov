package ru.otus.questionnaire;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.questionnaire.service.IOService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        final IOService ioService = context.getBean(IOService.class);
        ioService.print(System.out);
    }

}
