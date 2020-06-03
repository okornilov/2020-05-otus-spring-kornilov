package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование MessageBundleService")
@ExtendWith(MockitoExtension.class)
class MessageBundleServiceImplTest {

    @Mock
    private MessageSource messageSource;

    @Test
    void getMessage() {
        final String key = "key1";
        final String value = "value1";
        Mockito.when(messageSource.getMessage(Mockito.eq(key), Mockito.any(), Mockito.any(Locale.class))).thenReturn(value);
        MessageBundleService messageBundleService = new MessageBundleServiceImpl(messageSource, "ru");

        assertThat(messageBundleService.getMessage(key)).isEqualTo(value);
    }
}