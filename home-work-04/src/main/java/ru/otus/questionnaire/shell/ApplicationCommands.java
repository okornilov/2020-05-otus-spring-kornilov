package ru.otus.questionnaire.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.questionnaire.domain.User;
import ru.otus.questionnaire.service.AuthenticationService;
import ru.otus.questionnaire.service.MessageBundleService;
import ru.otus.questionnaire.service.QuestionService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {

    private final AuthenticationService authenticationService;
    private final MessageBundleService messageBundleService;
    private final QuestionService questionService;

    @ShellMethod(value = "Login user", key = {"l", "login"})
    @ShellMethodAvailability(value = "isNotAuthenticatedAvailability")
    public String login(@ShellOption String userName) {
        User user = authenticationService.authenticate(userName);
        return messageBundleService.getMessage("hello-message", user.getName());
    }

    @ShellMethod(value = "Start interview", key = {"i", "interview", "test"})
    @ShellMethodAvailability(value = "isAuthenticatedAvailability")
    public void interview() {
        questionService.startInterview();
    }

    private Availability isAuthenticatedAvailability() {
        return authenticationService.isAuthenticated() ? Availability.available() : Availability.unavailable("not authenticated");
    }

    private Availability isNotAuthenticatedAvailability() {
        return authenticationService.isAuthenticated() ? Availability.unavailable("user is authenticated") : Availability.available();
    }

}
