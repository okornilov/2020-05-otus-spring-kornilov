package ru.otus.questionnaire.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CSVReader {

    private CSVReader() {
    }

    public static List<String> load(String fileName) {
        final InputStream is = CSVReader.class.getResourceAsStream(fileName);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
