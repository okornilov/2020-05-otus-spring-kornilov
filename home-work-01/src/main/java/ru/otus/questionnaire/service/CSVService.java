package ru.otus.questionnaire.service;

import java.util.List;

public interface CSVService {
    void setFileName(String fileName);
    List<String> load();
}
