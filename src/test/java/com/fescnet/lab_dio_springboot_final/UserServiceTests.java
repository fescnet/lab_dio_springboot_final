package com.fescnet.lab_dio_springboot_final;

import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.UserRepository;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.impl.UserServiceImpl;
import com.fescnet.lab_dio_springboot_final.util.PojoProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import static org.mockito.Mockito.anyString;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
class UserServiceTests {

	@Autowired
	UserRepository userRepository;

	@Mock
	PasswordEncoder encoder;

	UserService userService;
	String encodedPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	@BeforeEach
	void setupDependencies(){
		userService = new UserServiceImpl(userRepository, encoder);
		Mockito.when(encoder.encode(anyString())).thenReturn(encodedPassword);
	}

	@Test
	void validUserCanBeCreated() {
		User user = PojoProvider.createValidUser();
		User userCreated = userService.create(user);
		Assertions.assertEquals(user.getEmail(), userCreated.getEmail());
	}

	@Test
	void passwordsAreStoredEncoded(){
		User user = PojoProvider.createValidUser();
		User userCreated = userService.create(user);
		Assertions.assertEquals(user.getPassword(), encodedPassword);
	}

	@Test
	void userWithNullEmailCannotBeCreatedAndThrowsException() {
		User user = PojoProvider.createValidUser();
		user.setEmail(null); // now, user is invalid
		Assertions.assertThrows(RuntimeException.class, () -> userService.create(user));
	}

	@Test
	void validUsersCannotBeCreatedWithTheSameEmail() {
		userService.create(PojoProvider.createValidUser());
		Assertions.assertThrows(RuntimeException.class, () -> userService.create(PojoProvider.createValidUser()));
	}
}
