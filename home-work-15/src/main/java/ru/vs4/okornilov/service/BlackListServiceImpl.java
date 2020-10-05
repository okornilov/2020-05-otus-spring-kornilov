package ru.vs4.okornilov.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.vs4.okornilov.domain.User;

import java.util.Random;

@Service("blackListService")
public class BlackListServiceImpl implements BlackListService {

    private final Random random = new Random();

    @SneakyThrows
    @Override
    public User check(User user) {
        Thread.sleep(1000L);
        System.out.println("Проверка по черным спискам...");
        user.setInBlackList(random.nextBoolean());
        return user;
    }
}
