package ru.otus.library.dao;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"ru.otus.library.mapper", "ru.otus.library.dao"})
public class TestDaoConfiguration {
}
