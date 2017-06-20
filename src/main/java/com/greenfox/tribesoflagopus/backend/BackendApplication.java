package com.greenfox.tribesoflagopus.backend;

import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	KingdomRepository kingdomRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello, World!");

		Kingdom kingdom = Kingdom.builder()
						.name("My new Kingdom")
						.userId(1L)
						.troops(new ArrayList<>())
						.build();

		Troop troop = Troop.builder()
						.attack(5)
						.defence(1)
						.hp(10)
						.level(1)
						.kingdom(kingdom)
						.build();

		kingdom.addTroop(troop);
		kingdomRepository.save(kingdom);
	}
}
