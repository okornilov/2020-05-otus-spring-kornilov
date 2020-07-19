package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Identifiable;

public interface BookRepositoryCustom {
    <T extends Identifiable> void addArrayElement(@NonNull String bookId, T element, String arrayName);

    <T extends Identifiable> void updateArrayElement(@NonNull String elementId, T element, String arrayName);

    <T> Page<T> findElementsByArrayName(String arrayName, String bookId, Pageable pageable, Class<T> tClass);

    void deleteElementInArray(@NonNull String id, String key);

    void deleteAllElementsInArray(String key);
}
