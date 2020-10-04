package ru.vs4.okornilov.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;

@SpringBootTest
class RegistrationServiceImplTest {

    @Autowired
    private RegistrationService registrationService;

    @Test
    void registration() {
        final User user = User.builder()
                .userName("test")
                .build();

        final Registration registration = registrationService.registration(user);
        Assertions.assertThat(registration)
                .isNotNull()
                .extracting(Registration::getUser)
                .isEqualTo(user);
    }
}