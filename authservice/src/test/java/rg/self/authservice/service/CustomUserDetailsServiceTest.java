package rg.self.authservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rg.self.authservice.entity.User;
import rg.self.authservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomUserDetailsService service;

    public CustomUserDetailsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User("user", "pass")));

        assertNotNull(service.loadUserByUsername("user"));
    }

    @Test
    void testLoadUserByUsernameFail() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.loadUserByUsername("user"));
    }
}
