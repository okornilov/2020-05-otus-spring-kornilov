package ru.vs4.okornilov.service;

import ru.vs4.okornilov.domain.User;

public interface BlackListService {
    User check(User user);
}
