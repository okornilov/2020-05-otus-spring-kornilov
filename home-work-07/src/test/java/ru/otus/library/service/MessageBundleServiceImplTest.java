package ru.otus.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование MessageBundleService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MessageBundleServiceImpl.class, DelegatingMessageSource.class})
class MessageBundleServiceImplTest {

    @Autowired
    private MessageBundleService bundleService;

    @Autowired
    private DelegatingMessageSource delegatingMessageSource;

    @MockBean
    private MessageSource messageSource;

    @BeforeEach
    void before() {
        delegatingMessageSource.setParentMessageSource(messageSource);
    }

    @DisplayName("Получить сообщение по ключу")
    @Test
    void getMessage() {
        String key = "message1";
        String value = "Message text 1";
        when(messageSource.getMessage(key, new Object[]{}, new Locale("en")))
                .thenReturn(value);

        assertThat(bundleService.getMessage(key))
                .isEqualTo(value);
    }

    @DisplayName("Получить сообщение по ключу и параметрам")
    @Test
    void getMessageArgs() {
        String key = "message2";
        String value = "Message text 2";
        Object[] args = new Object[]{ 123 };

        when(messageSource.getMessage(key, args, new Locale("en")))
                .thenReturn(value);

        assertThat(bundleService.getMessage(key, args))
                .isEqualTo(value);
    }
}