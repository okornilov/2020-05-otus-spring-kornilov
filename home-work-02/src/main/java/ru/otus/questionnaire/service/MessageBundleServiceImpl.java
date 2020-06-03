package ru.otus.questionnaire.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageBundleServiceImpl implements MessageBundleService {

    private final MessageSource messageSource;
    private final String locale;

    public MessageBundleServiceImpl(MessageSource messageSource, @Value("${locale:en}") String locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public String getMessage(String key) {
        return messageSource.getMessage(key, new Object[]{}, new Locale(locale));
    }

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, new Locale(locale));
    }
}
