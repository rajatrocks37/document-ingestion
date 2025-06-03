package rg.self.authservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rg.self.authservice.entity.User;
import rg.self.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public void deleteUser(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		userRepo.delete(user);
	}

}