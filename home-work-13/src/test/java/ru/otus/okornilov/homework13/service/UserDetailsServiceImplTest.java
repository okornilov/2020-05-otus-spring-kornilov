package ru.otus.okornilov.homework13.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.okornilov.homework13.domain.Authority;
import ru.otus.okornilov.homework13.domain.User;
import ru.otus.okornilov.homework13.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование UserDetailsService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void loadUserByUsernameTest() {
        User user = User.builder()
                .login("test")
                .enabled(true)
                .expired(false)
                .locked(false)
                .password("123")
                .authorities(Collections.singletonList(Authority.builder()
                        .authority("ROLE")
                        .build()))
                .build();

        when(userRepository.findById(eq("test")))
                .thenReturn(Optional.of(user));

        assertThat(userDetailsService.loadUserByUsername(user.getLogin()))
                .isEqualToComparingFieldByField(user);
    }
}