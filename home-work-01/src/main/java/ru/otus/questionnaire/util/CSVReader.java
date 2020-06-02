package ru.otus.questionnaire.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CSVReader {

    private List<String> lineList;
    private final String fileName;

    public List<String> load() {
        if (lineList == null) {
            final InputStream is = this.getClass().getResourceAsStream(fileName);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
                lineList = bufferedReader.lines().collect(Collectors.toList());
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
        return lineList;
    }

}
