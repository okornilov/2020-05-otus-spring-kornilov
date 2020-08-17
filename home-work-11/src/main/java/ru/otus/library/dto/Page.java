package ru.otus.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
public class Page<T> {

    public static final int FIRST_PAGE_NUM = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;

    Collection<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean first() {
        return pageNumber == FIRST_PAGE_NUM;
    }

    @JsonProperty
    public boolean last() {
        return (pageNumber + 1) * pageSize >= totalElements;
    }

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Page<>(this.getConvertedContent(converter), pageNumber, pageSize, totalElements);
    }

    private <U> Collection<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return content.stream().map(converter).collect(Collectors.toList());
    }

}