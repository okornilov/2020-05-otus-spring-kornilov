package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.User;
import ru.otus.library.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование UserServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("Получить пользователя по логину")
    @Test
    void loadUserByUsernameTest() {
        final User user = User.builder().login("test").build();

        when(userRepository.findById(user.getLogin()))
                .thenReturn(Optional.of(user));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLogin());

        assertThat(userDetails).isEqualToComparingFieldByField(user);
        verify(userRepository).findById(user.getLogin());
    }
}