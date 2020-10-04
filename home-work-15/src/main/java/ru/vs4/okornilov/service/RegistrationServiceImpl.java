package ru.vs4.okornilov.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;

@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {

    @SneakyThrows
    @Override
    public Registration registration(User user) {
        Thread.sleep(1000L);
        System.out.println("[SUCCESS] Успешная регистрация пользователя в системе ");
        return Registration.builder()
                .user(user)
                .success(true)
                .build();
    }
}
