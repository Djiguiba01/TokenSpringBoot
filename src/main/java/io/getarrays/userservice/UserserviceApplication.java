package io.getarrays.userservice;

import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UserserviceApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			Role r1=userService.saveRole(new Role(null, "ROLE_USER"));
			Role r2 = userService.saveRole(new Role(null, "ROLE_ADMIN"));
			//Role r3 = userService.saveRole(new Role(null, "ROLE_MANAGER"));
			//Role r4 = userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			User u1= userService.saveUser(new User(null, "djiguiba", "djiguibabarema@gmail.com", "64347072", new ArrayList<>()));
			User u2 = userService.saveUser(new User(null, "seydou", "seydou@gmail.com", "1234", new ArrayList<>()));
			User u3 = userService.saveUser(new User(null, "ali", "ali@gmail.com", "1234", new ArrayList<>()));
			User u4 = userService.saveUser(new User(null, "oumou", "oumougmail.com", "1234", new ArrayList<>()));
			User u5 = userService.saveUser(new User(null, "fanta", "fanta@gmail.com", "1234", new ArrayList<>()));

			userService.addRoleToUser(u1.getUsername(), r1.getName());
			userService.addRoleToUser(u1.getUsername(), r2.getName());
			userService.addRoleToUser(u2.getUsername(), r1.getName());
			userService.addRoleToUser(u3.getUsername(), r1.getName());
			userService.addRoleToUser(u4.getUsername(), r2.getName());
			userService.addRoleToUser(u5.getUsername(), r2.getName());

			/*userService.addRoleToUser("Mary", "ROLE_MANAGER");
			userService.addRoleToUser("testone", "ROLE_MANAGER");
			userService.addRoleToUser("testtwo", "ROLE_ADMIN");
			userService.addRoleToUser("testthree", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("testfour", "ROLE_ADMIN");
			userService.addRoleToUser("testfive", "ROLE_USER");*/

		};
	}
}
