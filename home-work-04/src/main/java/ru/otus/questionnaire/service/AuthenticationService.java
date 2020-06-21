package ru.otus.questionnaire.service;

import ru.otus.questionnaire.domain.User;

public interface AuthenticationService {
    User authenticate(String userName);
    boolean isAuthenticated();
    User getUser();
}
