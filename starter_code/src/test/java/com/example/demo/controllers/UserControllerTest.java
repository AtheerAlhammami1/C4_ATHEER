package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UserControllerTest {


    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
//        init w/ mockito
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }
    @Test
    public void createUserSuccess() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUser_UnmatchedPassword() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Atheer");
        request.setPassword("atheerPassword");
        request.setConfirmPassword("atheerAnotherPassword");
        final ResponseEntity<User> response = userController.createUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }
    @Test
    public void findByUserNameSuccess() {
        long id = 1;
        User user = new User();
        user.setUsername("Atheer");
        user.setPassword("atheerPassword");
        user.setId(id);
        when(userRepository.findByUsername("Atheer")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("Atheer");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals("Atheer", u.getUsername());
    }

    @Test
    public void findByIdSuccess() {
        long id = 1;
        User user = new User();
        user.setUsername("Atheer");
        user.setPassword("atheerPassword");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        final ResponseEntity<User> response = userController.findById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("Atheer", u.getUsername());
    }
}
