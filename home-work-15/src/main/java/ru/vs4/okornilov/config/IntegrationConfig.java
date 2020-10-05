package ru.vs4.okornilov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;

@Configuration
public class IntegrationConfig {

    @Bean
    public DirectChannel regRequestChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel reqResponseChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel blackListChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel checkPasswordPolicy() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel noValidaPasswordChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel registrationChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public IntegrationFlow checkBlackListFlow() {
        return IntegrationFlows.from("regRequestChannel")
                .handle("blackListService", "check")
                .<User, Boolean>route(User::isInBlackList, mapping -> mapping
                                .subFlowMapping(true, sf -> sf.channel("blackListChannel"))
                                .subFlowMapping(false, sf -> sf.channel("checkPasswordPolicy")))
                .get();
    }

    @Bean
    public IntegrationFlow blackListFlow() {
        return IntegrationFlows.from("blackListChannel")
                .<User, Registration>transform(user -> Registration.builder()
                        .user(user)
                        .success(false)
                        .build())
                .channel("reqResponseChannel")
                .get();

    }

    @Bean
    public IntegrationFlow noValidaPasswordFlow() {
        return IntegrationFlows.from("noValidaPasswordChannel")
                .<User, Registration>transform(user -> Registration.builder()
                        .user(user)
                        .success(false)
                        .build())
                .channel("reqResponseChannel")
                .get();

    }

    @Bean
    public IntegrationFlow checkPasswordPolicyFlow() {
        return IntegrationFlows.from("checkPasswordPolicy")
                .handle("passwordPolicyService", "check")
                .<User, Boolean>route(User::isValidPassword, mapping -> mapping
                    .subFlowMapping(true, sf -> sf.channel("noValidaPasswordChannel"))
                    .subFlowMapping(false, sf -> sf.channel("registrationChannel")))
                .get();
    }

    @Bean
    public IntegrationFlow registrationFlow() {
        return IntegrationFlows.from("registrationChannel")
                .handle("registrationService", "registration")
                .channel("reqResponseChannel")
                .get();
    }


}
