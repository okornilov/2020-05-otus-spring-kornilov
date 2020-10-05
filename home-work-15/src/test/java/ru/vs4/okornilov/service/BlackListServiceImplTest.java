package ru.vs4.okornilov.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vs4.okornilov.domain.User;

@SpringBootTest
class BlackListServiceImplTest {

    @Autowired
    private BlackListService blackListService;

    @Test
    void check() {
        final User user = blackListService.check(User.builder().build());
        Assertions.assertThat(user).isNotNull();
    }
}