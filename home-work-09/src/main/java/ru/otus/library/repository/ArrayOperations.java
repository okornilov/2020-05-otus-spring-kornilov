package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Identifiable;

public interface ArrayOperations {

    <T extends Identifiable> void saveElement(@NonNull String objectId, T element, String arrayName);

    <T> Page<T> findElements(String arrayName, String objectId, Pageable pageable, Class<T> dtoClass);

    <T> T findById(String arrayName, String id, Class<T> dtoClass);

    void deleteElement(@NonNull String id, String key);
}
