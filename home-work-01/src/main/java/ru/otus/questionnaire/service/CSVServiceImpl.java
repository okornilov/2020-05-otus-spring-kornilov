package ru.otus.questionnaire.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CSVServiceImpl implements CSVService {

    private String fileName;

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> load() {
        final InputStream is = this.getClass().getResourceAsStream(fileName);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
