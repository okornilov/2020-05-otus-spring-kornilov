package ru.otus.okornilov.homework14.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.okornilov.homework14.domain.Author;
import ru.otus.okornilov.homework14.domain.Book;
import ru.otus.okornilov.homework14.domain.Genre;
import ru.otus.okornilov.homework14.reader.AggregateMongoReader;

import javax.sql.DataSource;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfig {
    public static final String CONVERT_BOOK_JOB_NAME = "convertBookJob";
    private static final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job convertBookJob(@Qualifier("createTempTables") Step createTempTables,
                              @Qualifier("importTmpBooks") Step importTmpBooks,
                              @Qualifier("importTmpGenres") Step importTmpGenres,
                              @Qualifier("importTmpAuthors") Step importTmpAuthors,
                              @Qualifier("importBooks") Step importBooks,
                              @Qualifier("importAuthors") Step importAuthors,
                              @Qualifier("importBookAuthors") Step importBookAuthors,
                              @Qualifier("importGenres") Step importGenres,
                              @Qualifier("importBookGenres") Step importBookGenres,
                              @Qualifier("dropTempTables") Step dropTempTables) {
        return jobBuilderFactory.get(CONVERT_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(createTempTables)
                .next(importTmpAuthors)
                .next(importTmpGenres)
                .next(importTmpBooks)
                .next(importBooks)
                .next(importAuthors)
                .next(importBookAuthors)
                .next(importGenres)
                .next(importBookGenres)
                .next(dropTempTables)
                .build();
    }


    // steps
    @Bean
    public Step importTmpAuthors(AggregateMongoReader<Author> authorAggregateMongoReader,
                                 @Qualifier("tmpAuthorWriter") ItemWriter<Author> tmpAuthorWriter) {
        return stepBuilderFactory.get("importTmpAuthors")
                .<Author, Author>chunk(CHUNK_SIZE)
                .reader(authorAggregateMongoReader)
                .writer(tmpAuthorWriter)
                .build();
    }

    @Bean
    public Step importTmpGenres(AggregateMongoReader<Genre> genreReader,
                                @Qualifier("tmpGenreWriter") ItemWriter<Genre> tmpGenreWriter) {
        return stepBuilderFactory.get("importTmpGenres")
                .<Genre, Genre>chunk(CHUNK_SIZE)
                .reader(genreReader)
                .writer(tmpGenreWriter)
                .build();
    }

    @Bean
    public Step importTmpBooks(@Qualifier("mongoBookReader")MongoItemReader<Book> mongoBookReader,
                               @Qualifier("tmpBookWriter") ItemWriter<Book> bookTempWriter) {
        return stepBuilderFactory.get("importTmpBooks")
                .<Book, Book>chunk(CHUNK_SIZE)
                .reader(mongoBookReader)
                .writer(bookTempWriter)
                .build();
    }

    @Bean
    public Step importBooks(@Qualifier("tmpBookReader") ItemReader<Book> tmpBookReader,
                          @Qualifier("bookWriter") ItemWriter<Book> bookWriter) {
        return stepBuilderFactory.get("importBooks")
                .<Book, Book>chunk(CHUNK_SIZE)
                .reader(tmpBookReader)
                .writer(bookWriter)
                .build();
    }

    @Bean
    public Step importAuthors(@Qualifier("tmpAuthorReader") ItemReader<Author> tmpAuthorReader,
                              @Qualifier("authorWriter") ItemWriter<Author> authorWriter) {
        return stepBuilderFactory.get("importTmpAuthors")
                .<Author, Author>chunk(CHUNK_SIZE)
                .reader(tmpAuthorReader)
                .writer(authorWriter)
                .build();
    }

    @Bean
    public Step importBookAuthors(@Qualifier("tmpAuthorReader") ItemReader<Author> tmpAuthorReader,
                              @Qualifier("bookAuthorsWriter") ItemWriter<Author> bookAuthorsWriter) {
        return stepBuilderFactory.get("importBookAuthors")
                .<Author, Author>chunk(CHUNK_SIZE)
                .reader(tmpAuthorReader)
                .writer(bookAuthorsWriter)
                .build();
    }

    @Bean
    public Step importGenres(@Qualifier("tmpGenreReader") ItemReader<Genre> tmpGenreReader,
                              @Qualifier("genreWriter") ItemWriter<Genre> genreWriter) {
        return stepBuilderFactory.get("importGenres")
                .<Genre, Genre>chunk(CHUNK_SIZE)
                .reader(tmpGenreReader)
                .writer(genreWriter)
                .build();
    }

    @Bean
    public Step importBookGenres(@Qualifier("tmpGenreReader") ItemReader<Genre> tmpGenreReader,
                              @Qualifier("bookGenresWriter") ItemWriter<Genre> bookGenresWriter) {
        return stepBuilderFactory.get("importBookGenres")
                .<Genre, Genre>chunk(CHUNK_SIZE)
                .reader(tmpGenreReader)
                .writer(bookGenresWriter)
                .build();
    }

    @Bean
    public Step createTempTables(JdbcTemplate jdbcTemplate) {
        return this.stepBuilderFactory.get("createTempTables").tasklet((stepContribution, chunkContext) -> {
            jdbcTemplate.execute("drop table if exists tmp_authors cascade");
            jdbcTemplate.execute("drop table if exists tmp_genres cascade");
            jdbcTemplate.execute("drop table if exists tmp_books cascade");

            jdbcTemplate.execute("create table tmp_books (" +
                    "id bigint auto_increment primary key," +
                    "ext_id varchar(36) not null," +
                    "name varchar(255) not null" +
                    ")");


            jdbcTemplate.execute("create table tmp_authors (" +
                    "id bigint auto_increment primary key," +
                    "ext_id varchar(36) not null," +
                    "full_name varchar(255) not null," +
                    "book_id varchar(36)" +
                    ")");

            jdbcTemplate.execute("create table tmp_genres (" +
                    "id bigint auto_increment primary key," +
                    "ext_id varchar(36) not null," +
                    "name varchar(255) not null," +
                    "book_id varchar(36)" +
                    ")");

            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step dropTempTables(JdbcTemplate jdbcTemplate) {
        return stepBuilderFactory.get("dropTempTables").tasklet((stepContribution, chunkContext) -> {
            jdbcTemplate.execute("drop table tmp_authors cascade");
            jdbcTemplate.execute("drop table tmp_genres cascade");
            jdbcTemplate.execute("drop table tmp_books cascade");
            return RepeatStatus.FINISHED;
        }).build();
    }

    // readers
    @Bean
    public AggregateMongoReader<Genre> genreReader(MongoTemplate mongoTemplate) {
        final AggregateMongoReader<Genre> reader = new AggregateMongoReader<>(mongoTemplate, "genres", Genre.class);
        reader.setName("genreReader");
        return reader;
    }

    @Bean
    public AggregateMongoReader<Author> authorReader(MongoTemplate mongoTemplate) {
        final AggregateMongoReader<Author> reader = new AggregateMongoReader<>(mongoTemplate, "authors", Author.class);
        reader.setName("authorReader");
        return reader;
    }

    @Bean
    public MongoItemReader<Book> mongoBookReader(MongoTemplate mongoTemplate) {
        MongoItemReader<Book> mongoItemReader = new MongoItemReader<>();
        mongoItemReader.setTemplate(mongoTemplate);
        mongoItemReader.setName("mongoBookReader");
        mongoItemReader.setQuery("{}");
        mongoItemReader.setTargetType(Book.class);
        mongoItemReader.setSort(Map.of("_id", Sort.Direction.ASC));
        return mongoItemReader;
    }

    @Bean
    public ItemReader<Book> tmpBookReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Book>()
                .name("tmpBookReader")
                .dataSource(dataSource)
                .sql("SELECT id, name FROM tmp_books")
                .rowMapper(new BeanPropertyRowMapper<>(Book.class))
                .build();
    }

    @Bean
    public ItemReader<Author> tmpAuthorReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Author>()
                .name("tmpAuthorReader")
                .dataSource(dataSource)
                .sql("SELECT a.id as id, a.full_name as fullName, b.id as bookId " +
                        "FROM tmp_authors a left join tmp_books b on a.book_id = b.ext_id")
                .rowMapper(new BeanPropertyRowMapper<>(Author.class))
                .build();
    }

    @Bean
    public ItemReader<Genre> tmpGenreReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Genre>()
                .name("tmpGenreReader")
                .dataSource(dataSource)
                .sql("SELECT a.id as id, a.name as name, b.id as bookId " +
                        "FROM tmp_genres a left join tmp_books b on a.book_id = b.ext_id")
                .rowMapper(new BeanPropertyRowMapper<>(Genre.class))
                .build();
    }

    // writers
    @Bean
    public ItemWriter<Author> tmpAuthorWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO tmp_authors " +
                "(ext_id, full_name, book_id) " +
                "VALUES (:id, :fullName, :bookId)");
        writer.setDataSource(dataSource);
        return writer;
    }


    @Bean
    public ItemWriter<Genre> tmpGenreWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO tmp_genres " +
                "(ext_id, name, book_id) " +
                "VALUES (:id, :name, :bookId)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Book> tmpBookWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO tmp_books " +
                "(ext_id, name) " +
                "VALUES (:id, :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Book> bookWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO books " +
                "(id, name) " +
                "VALUES (:id, :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Author> authorWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO authors " +
                "(id, full_name) " +
                "VALUES (:id, :fullName)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Author> bookAuthorsWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO book_authors " +
                "(author_id, book_id) " +
                "VALUES (:id, :bookId)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Genre> genreWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO genres " +
                "(id, name) " +
                "VALUES (:id, :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Genre> bookGenresWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO book_genres " +
                "(genre_id, book_id) " +
                "VALUES (:id, :bookId)");
        writer.setDataSource(dataSource);
        return writer;
    }
}
