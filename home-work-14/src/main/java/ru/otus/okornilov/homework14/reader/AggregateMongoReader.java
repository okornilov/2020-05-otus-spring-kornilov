package ru.otus.okornilov.homework14.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.util.Assert;
import ru.otus.okornilov.homework14.domain.Book;

import java.util.Iterator;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class AggregateMongoReader<T> extends AbstractPaginatedDataItemReader<T> implements InitializingBean {

    private final MongoTemplate mongoTemplate;
    private final String nestedCollection;
    private final Class<? extends T> targetClass;

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<T> doPageRead() {
        final AddFieldsOperation addFields = addFields()
                .addFieldWithValue(nestedCollection + ".bookId", "$_id")
                .build();

        final Aggregation aggregation = newAggregation(unwind(nestedCollection),
                addFields,
                replaceRoot(nestedCollection),
                skip((long) pageSize * page),
                limit(pageSize));

        return (Iterator<T>) this.mongoTemplate
                .aggregate(aggregation, Book.class, targetClass)
                .iterator();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(mongoTemplate != null, "An implementation of MongoOperations is required.");
        Assert.state(nestedCollection != null, "A nested collection name is required.");
        Assert.state(targetClass != null, "A type to convert the input into is required.");
    }
}
