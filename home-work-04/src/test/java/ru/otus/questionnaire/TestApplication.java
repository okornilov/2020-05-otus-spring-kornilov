package ru.otus.questionnaire;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.otus.questionnaire.shell.ApplicationCommands;

@SpringBootConfiguration
@ComponentScan(basePackages = "ru.otus.questionnaire", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ApplicationCommands.class))
public class TestApplication {
}
