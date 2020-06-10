package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.questionnaire.config.TestConfig;
import ru.otus.questionnaire.mettaannotaions.TestProfile;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование MessageBundleService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = {MessageBundleServiceImpl.class, TestConfig.class})
@TestProfile
class MessageBundleServiceImplTest {

    @Autowired
    private MessageBundleService messageBundleService;

    @Test
    void getMessage() {
        assertThat(messageBundleService.getMessage("message1"))
                .isEqualTo("Message Text");
    }

    @Test
    void getMessageArgs() {
        assertThat(messageBundleService.getMessage("message2", 123))
                .isEqualTo("Message with argument = 123");
    }
}