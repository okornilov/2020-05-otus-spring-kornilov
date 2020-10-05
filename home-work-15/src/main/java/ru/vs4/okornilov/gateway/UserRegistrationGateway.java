package ru.vs4.okornilov.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;

@MessagingGateway
public interface UserRegistrationGateway {

    @Gateway(requestChannel = "regRequestChannel", replyChannel = "reqResponseChannel")
    Registration registration(User user);
}
