package ru.vs4.okornilov.service;

import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;

public interface RegistrationService {
    Registration registration(User user);
}
