package com.crio.RentRead.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crio.RentRead.Dto.UserRegistrationDto;
import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Exceptions.EmailAlreadyInUseException;
import com.crio.RentRead.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserSuccess() {
        UserRegistrationDto userdto = new UserRegistrationDto("john.doe@example.com", "password", "John", "Doe", null);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setRoles("ROLE_USER");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(userdto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("ROLE_USER", result.getRoles());
        verify(userRepository).save(user);
    }

    @Test
    public void testRegisterUserEmailAlreadyInUse() {
        UserRegistrationDto userdto = new UserRegistrationDto("john.doe@example.com", "password", "John", "Doe", null);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setRoles("ROLE_USER");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        doThrow(new DataIntegrityViolationException("Duplicate entry")).when(userRepository).save(user);

        Exception exception = assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.registerUser(userdto);
        });

        assertEquals("Email is already in use: john.doe@example.com", exception.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getallUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);

        User result = userService.getUser("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).findByEmail("john.doe@example.com");
    }
}

