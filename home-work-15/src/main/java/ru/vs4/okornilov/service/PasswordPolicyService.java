package ru.vs4.okornilov.service;

import ru.vs4.okornilov.domain.User;

public interface PasswordPolicyService {
    User check(User user);
}
