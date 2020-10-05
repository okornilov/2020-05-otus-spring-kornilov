package ru.vs4.okornilov.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.vs4.okornilov.domain.User;

import java.util.Random;

@Service("passwordPolicyService")
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    private final Random random = new Random();

    @SneakyThrows
    @Override
    public User check(User user) {
        Thread.sleep(1000L);
        System.out.println("Проверка пароля на соответствие политики безопасности...");
        user.setValidPassword(random.nextBoolean());
        return user;
    }
}
