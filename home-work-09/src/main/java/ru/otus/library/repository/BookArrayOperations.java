package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Identifiable;
import ru.otus.library.dto.CountDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class BookArrayOperations implements ArrayOperations {
    private final MongoTemplate mongoTemplate;

    private   <T extends Identifiable> void addElement(@NonNull String objectId, T element, String arrayName) {
        element.setId(UUID.randomUUID().toString());
        Update update = new Update().push(arrayName).value(element);
        this.mongoTemplate.updateMulti(new Query(Criteria.where("_id").is(objectId)), update, Book.class);
    }

    private  <T extends Identifiable> void updateElement(@NonNull String elementId, T element, String arrayName) {
        element.setId(elementId);
        Update update = new Update()
                .filterArray("elementId._id", element.getId())
                .set(arrayName + ".$[elementId]", element);
        this.mongoTemplate.updateMulti(new Query(Criteria.where(arrayName).exists(true)), update, Book.class);
    }

    @Override
    public <T extends Identifiable> void saveElement(@NonNull String objectId, T element, String arrayName) {
        if (StringUtils.isEmpty(element.getId())) {
            addElement(objectId, element, arrayName);
        } else {
            updateElement(element.getId(), element, arrayName);
        }
    }

    @Override
    public  <T> Page<T> findElements(String arrayName, String bookId, Pageable pageable, Class<T> dtoClass) {
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

        final CountDto countDto = Optional.ofNullable(mongoTemplate.aggregate(aggregationCount, Book.class, CountDto.class)
                .getUniqueMappedResult()).orElse(new CountDto(0L));
        final List<T> resultList = this.mongoTemplate.aggregate(aggregation, Book.class, dtoClass).getMappedResults();

        return new PageImpl<>(resultList, pageable, countDto.getTotal());
    }

    @Override
    public <T> T findById(String arrayName, String id, Class<T> dtoClass) {
        return mongoTemplate.aggregate(newAggregation(unwind(arrayName), replaceRoot(arrayName), match(Criteria.where("_id").is(id))),
                Book.class, dtoClass).getUniqueMappedResult();
    }

    public void deleteElement(@NonNull String id, String key) {
        final Update update = new Update().pull(key, Query.query(Criteria.where("_id").is(id)));
        mongoTemplate.updateMulti(new Query(), update, Book.class);
    }

}
