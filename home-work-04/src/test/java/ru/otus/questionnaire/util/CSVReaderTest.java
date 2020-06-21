package ru.otus.questionnaire.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Тестирование CSVReader")
class CSVReaderTest {

    @DisplayName("Загрузка csv файла")
    @Test
    void load() {
        CSVReader csvReader = new CSVReader();
        List<String> stringList = csvReader.load("/questions-test.csv");
        Assertions.assertThat(stringList)
                .isNotEmpty()
                .asList()
                .hasSize(1)
                .element(0).asString().contains("Test question");
    }
}