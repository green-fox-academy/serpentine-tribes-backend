package com.greenfox.tribesoflagopus.backend;

import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello, World!");

			User user = User.builder()
							.username("Noemi")
							.password("passnoemi")
							.build();

			Kingdom kingdom = Kingdom.builder()
							.name("Noemi's Kingdom")
							.build();

			user.setKingdom(kingdom);
			kingdom.setUser(user);

			userRepository.save(user);
	}
}
