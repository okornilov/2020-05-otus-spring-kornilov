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
        final CSVReader csvReader = new CSVReader("/questions-test.csv");
        final List<String> stringList = csvReader.load();
        Assertions.assertThat(stringList)
                .isNotEmpty()
                .asList()
                .hasSize(1)
                .element(0).asString().contains("Test question");
    }
}