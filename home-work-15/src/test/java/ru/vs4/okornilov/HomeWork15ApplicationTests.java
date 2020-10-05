package ru.vs4.okornilov;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vs4.okornilov.domain.Registration;
import ru.vs4.okornilov.domain.User;
import ru.vs4.okornilov.gateway.UserRegistrationGateway;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HomeWork15ApplicationTests {

	@Autowired
	private UserRegistrationGateway gateway;

	@Test
	void contextLoads() {
		final Registration registration = gateway.registration(User.builder()
				.email("mail@mail.ru")
				.userName("Ivan")
				.password("12345678")
				.build());

		assertThat(registration)
				.isNotNull();
	}

}
