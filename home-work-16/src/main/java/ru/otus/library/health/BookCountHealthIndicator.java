package ru.otus.library.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.library.repository.BookRepository;

@RequiredArgsConstructor
@Component
public class BookCountHealthIndicator implements HealthIndicator {

    public static final String TOTAL = "total";
    private final BookRepository bookRepository;

    @Override
    public Health health() {
        return Health.up()
                .withDetail(TOTAL, bookRepository.count())
                .build();
    }
}
