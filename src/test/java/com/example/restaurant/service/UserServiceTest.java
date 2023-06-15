package com.example.restaurant.service;

import com.example.restaurant.dto.UserDTO;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.Role;
import com.example.restaurant.exception.UserExistException;
import com.example.restaurant.payload.request.SignupRequest;
import com.example.restaurant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstname("John");
        signupRequest.setLastname("Doe");
        signupRequest.setUsername("johndoe");
        signupRequest.setPassword("password");

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstname(signupRequest.getFirstname());
        user.setLastname(signupRequest.getLastname());
        user.setUsername(signupRequest.getUsername());
        user.setPassword("encodedPassword");
        user.getRoles().add(Role.CLIENT);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        User createdUser = userService.createUser(signupRequest);

        assertNotNull(createdUser);
        assertEquals(signupRequest.getEmail(), createdUser.getEmail());
        assertEquals(signupRequest.getFirstname(), createdUser.getFirstname());
        assertEquals(signupRequest.getLastname(), createdUser.getLastname());
        assertEquals(signupRequest.getUsername(), createdUser.getUsername());
        assertTrue(createdUser.getRoles().contains(Role.CLIENT));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UserExistException() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existingUser");

        when(userRepository.findUserByUsername(signupRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UserExistException.class, () -> userService.createUser(signupRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserById(userId);

        assertNotNull(retrievedUser);
        assertEquals(user, retrievedUser);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFoundException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO("John", "Doe");

        User user = new User();
        Principal principal = mock(Principal.class);

        when(userRepository.findUserByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(userDTO, principal);

        assertNotNull(updatedUser);
        assertEquals(userDTO.getFirstname(), updatedUser.getFirstname());
        assertEquals(userDTO.getLastname(), updatedUser.getLastname());

        verify(userRepository, times(1)).findUserByUsername(principal.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_UserNotFoundException() {
        UserDTO userDTO = new UserDTO("John", "Doe");
        Principal principal = mock(Principal.class);

        when(userRepository.findUserByUsername(principal.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(userDTO, principal));

        verify(userRepository, times(1)).findUserByUsername(principal.getName());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        Principal principal = mock(Principal.class);

        when(userRepository.findUserByUsername(principal.getName())).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser(principal);

        assertNotNull(currentUser);
        assertEquals(user, currentUser);

        verify(userRepository, times(1)).findUserByUsername(principal.getName());
    }

    @Test
    void testGetCurrentUser_UserNotFoundException() {
        Principal principal = mock(Principal.class);

        when(userRepository.findUserByUsername(principal.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getCurrentUser(principal));

        verify(userRepository, times(1)).findUserByUsername(principal.getName());
    }
}
