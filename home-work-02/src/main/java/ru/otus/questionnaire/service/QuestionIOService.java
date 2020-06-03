package ru.otus.questionnaire.service;

import java.io.PrintStream;
import java.util.Scanner;

public class QuestionIOService implements IOService {

    private final PrintStream printStream;
    private final Scanner scanner;

    public QuestionIOService(PrintStream printStream, Scanner scanner) {
        this.printStream = printStream;
        this.scanner = scanner;
    }

    @Override
    public void print(String s) {
        printStream.println(s);
    }

    @Override
    public String read() {
        return scanner.next();
    }
}