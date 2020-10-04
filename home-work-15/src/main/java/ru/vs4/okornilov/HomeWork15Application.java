package ru.vs4.okornilov;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import ru.vs4.okornilov.domain.User;
import ru.vs4.okornilov.gateway.UserRegistrationGateway;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication
@IntegrationComponentScan
public class HomeWork15Application {

	@Qualifier("blackListChannel")
	private final PublishSubscribeChannel blackListChannel;

	@Qualifier("noValidaPasswordChannel")
	private final PublishSubscribeChannel noValidaPasswordChannel;

	@PostConstruct
	public void subscribeOnChannels() {
		blackListChannel.subscribe(message ->
				System.out.printf("[BLACK LIST] Пользователь %s  находится в черном списке ", ((User) message.getPayload()).getUserName()));
		noValidaPasswordChannel.subscribe(message ->
				System.out.printf("[NO VALID PASSWORD] Пользователь %s  не прошел проверку пароля ", ((User) message.getPayload()).getUserName()));
	}



	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(HomeWork15Application.class, args);
		final UserRegistrationGateway gateway = context.getBean(UserRegistrationGateway.class);

		final User user1 = User.builder()
				.userName("john")
				.email("john@gmail.com")
				.password("123")
				.build();

		final User user2 = User.builder()
				.userName("max")
				.email("max@gmail.com")
				.password("12345678")
				.build();


		for (int i = 0; i < 10; i++) {
			System.out.println(gateway.registration(user1));
			System.out.println(gateway.registration(user2));
		}
	}

}
