package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Identifiable;
import ru.otus.library.dto.CountDto;
import ru.otus.library.dto.Page;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookArrayOperations implements ArrayOperations {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public <T extends Identifiable> Mono<T> addElement(@NonNull String objectId, T element, String arrayName) {
        element.setId(UUID.randomUUID().toString());
        Update update = new Update().push(arrayName).value(element);
        return this.mongoTemplate
                .updateMulti(new Query(Criteria.where("_id").is(objectId)), update, Book.class)
                .map(r -> element);
    }

    @Override
    public <T extends Identifiable> Mono<T> updateElement(@NonNull String elementId, T element, String arrayName) {
        element.setId(elementId);
        Update update = new Update()
                .filterArray("elementId._id", element.getId())
                .set(arrayName + ".$[elementId]", element);
        return this.mongoTemplate
                .updateMulti(new Query(Criteria.where(arrayName).exists(true)), update, Book.class)
                .map(r -> element);
    }

    @Override
    public  <T> Mono<Page<T>> findElements(String arrayName, String bookId, Pageable pageable, Class<T> dtoClass) {
        final UnwindOperation unwind = unwind(arrayName);
        final AddFieldsOperation addFields = addFields().addFieldWithValue(arrayName + ".bookId", "$_id")
                .addFieldWithValue(arrayName + ".bookName", "$name")
                .build();

        final ReplaceRootOperation replaceRoot = replaceRoot(arrayName);
        final CountOperation count = count().as("total");
        final SkipOperation skip = skip((long) pageable.getPageSize() * pageable.getPageNumber());
        final LimitOperation limit = limit(pageable.getPageSize());
        MatchOperation match = null;

        if (bookId != null) {
            match = match(Criteria.where("_id").is(bookId));
        }

        final Aggregation aggregationCount = match == null ? newAggregation(unwind, count) : newAggregation(match, unwind, count);
        final Aggregation aggregation = match == null ? newAggregation(unwind, addFields, replaceRoot, skip, limit) :
                newAggregation(match, unwind, addFields, replaceRoot, skip, limit);

        return this.mongoTemplate.aggregate(aggregation, Book.class, dtoClass)
                .collectList()
                .flatMap(elements ->
                        mongoTemplate.aggregate(aggregationCount, Book.class, CountDto.class)
                                .next()
                                .map(v -> new Page<>(elements, pageable.getPageNumber(), pageable.getPageSize(), v.getTotal()))
                )
                .switchIfEmpty(
                        Mono.just(new Page<>(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize() ,0))
                );
    }

    public Mono<Void> deleteElement(@NonNull String id, String key) {
        final Update update = new Update().pull(key, Query.query(Criteria.where("_id").is(id)));
        return mongoTemplate
                .updateMulti(new Query(), update, Book.class)
                .then();
    }

}
