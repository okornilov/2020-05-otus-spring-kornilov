package ru.otus.questionnaire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.questionnaire.TestApplication;
import ru.otus.questionnaire.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование AuthenticationService")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = TestApplication.class)
class AuthenticationServiceImplTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void authenticate() {
        User user = authenticationService.authenticate("Ivan");
        assertThat(user).extracting(User::getName).isEqualTo("Ivan");
    }

    @Test
    void isNotAuthenticated() {
        assertThat(authenticationService.isAuthenticated()).isFalse();
    }

    @Test
    void isAuthenticated() {
        authenticationService.authenticate("Ivan");
        assertThat(authenticationService.isAuthenticated()).isTrue();
    }

    @Test
    void getUser() {
        User user = new User("John");
        authenticationService.authenticate(user.getName());
        assertThat(authenticationService.getUser())
                .isEqualTo(user);
    }
}