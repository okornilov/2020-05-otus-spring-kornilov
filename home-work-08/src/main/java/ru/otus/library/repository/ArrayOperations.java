package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Identifiable;

public interface ArrayOperations {

    <T extends Identifiable> void addArrayElement(@NonNull String objectId, T element, String arrayName);

    <T extends Identifiable> void updateArrayElement(@NonNull String elementId, T element, String arrayName);

    <T> Page<T> findElementsByArrayName(String arrayName, String objectId, Pageable pageable, Class<T> dtoClass);

    void deleteElementInArray(@NonNull String id, String key);

    void deleteAllElementsInArray(String key);

}
