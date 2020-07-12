package ru.otus.library.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

@DisplayName("Тестирование LibraryIOService")
@ExtendWith(SpringExtension.class)
class LibraryIOServiceTest {

    @DisplayName("Вывести строку")
    @Test
    void outLine() {
        final Optional<Object> optional = Optional.ofNullable(null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOService ioService = new LibraryIOService(new PrintStream(baos));
        String line = "output string";
        ioService.outLine(line);

        Assertions.assertThat(baos.toString())
                .isEqualToIgnoringNewLines(line);
    }
}