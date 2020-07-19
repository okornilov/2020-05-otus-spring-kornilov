package ru.otus.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Comment implements Identifiable {
    private String id;
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now().withNano(0);
    private String text;
}
