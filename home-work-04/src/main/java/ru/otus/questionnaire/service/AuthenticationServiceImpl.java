package ru.otus.questionnaire.service;

import org.springframework.stereotype.Service;
import ru.otus.questionnaire.domain.User;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private User user;

    @Override
    public User authenticate(String userName) {
        user = new User(userName);
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    @Override
    public User getUser() {
        return user;
    }
}
