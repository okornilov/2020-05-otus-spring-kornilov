package ru.otus.questionnaire.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Тестирование CSVService")
class CSVServiceImplTest {

    @DisplayName("Загрузка файла")
    @Test
    void load() {
        CSVService csvService = new CSVServiceImpl();
        csvService.setFileName("/questions.csv");
        final List<String> stringList = csvService.load();

        Assertions.assertThat(stringList).isNotEmpty();
    }
}