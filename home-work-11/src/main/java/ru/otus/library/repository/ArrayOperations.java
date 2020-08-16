package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Identifiable;
import ru.otus.library.dto.Page;

public interface ArrayOperations {

    <T extends Identifiable> Mono<T> addElement(@NonNull String objectId, T element, String arrayName);

    <T extends Identifiable> Mono<T> updateElement(@NonNull String elementId, T element, String arrayName);

    <T> Mono<Page<T>> findElements(String arrayName, String objectId, Pageable pageable, Class<T> dtoClass);

    Mono<Void> deleteElement(@NonNull String id, String key);
}
