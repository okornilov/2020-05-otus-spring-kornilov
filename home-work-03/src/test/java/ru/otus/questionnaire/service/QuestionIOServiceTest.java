package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование QuestionIOService")
class QuestionIOServiceTest {

    @Test
    void print() {
        final String outputString = "myOutputString";
        OutputStream os = new ByteArrayOutputStream();
        final Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[]{}));
        final PrintStream printStream = new PrintStream(os);
        QuestionIOService questionIOService = new QuestionIOService(printStream, scanner);
        questionIOService.print(outputString);

        assertThat(os.toString()).contains(outputString);
    }

    @Test
    void read() {
        final String inputString = "myInputString";
        final Scanner scanner = new Scanner(new ByteArrayInputStream(inputString.getBytes()));
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        QuestionIOService questionIOService = new QuestionIOService(printStream, scanner);

        assertThat(questionIOService.read()).contains(inputString);
    }
}