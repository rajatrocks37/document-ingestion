package rg.self.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rg.self.authservice.entity.Users;

public interface UserRepository extends JpaRepository<Users, String> {
	Optional<Users> findByUsername(String username);

	void deleteByUsername(String username);
}