package com.example.demo;

import com.example.demo.entities.AppUser;
import com.example.demo.entities.Journal;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private AppUserRepository appUserRepository;

	@Profile({"dev", "prod"})
	@Bean
	public CommandLineRunner run(JournalRepository journalRepository, AppUserRepository userRepository) {
		return args -> {
			// Example AppUser, you can replace with actual user fetching or registration logic
			AppUser appUser = new AppUser();
			appUser.setEmail("user@example.com");
			appUser.setPassword("password");  // Should be encoded using bcrypt or similar
			userRepository.save(appUser);

			// Create a journal associated with the AppUser
			Journal journal = new Journal();
			journal.setTitle("Dan's First Journal");
			journal.setContent("This is Dan's First Journal entry.");
			journal.setAppUser(appUser);

			journalRepository.save(journal);
		};
	}


}
