package rg.self.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rg.self.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByUsername(String username);

	void deleteByUsername(String username);
}