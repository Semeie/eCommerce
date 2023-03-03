package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController,"userRepository",userRepository);
        TestUtils.injectObject(userController,"cartRepository", cartRepository);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path(){
        final ResponseEntity<User> responseEntity = createTestUser();

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();

        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        assertEquals("thisIsHashed",user.getPassword());
    }

    @Test
    public void test_find_user_by_id(){
        final ResponseEntity<User> responseEntity = createTestUser();
        User user = responseEntity.getBody();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final ResponseEntity<User> findUserById = userController.findById(user.getId());
        User userFound = findUserById.getBody();
        assertEquals(200, findUserById.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("test", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());

    }

    @Test
    public void test_find_user_by_username() throws Exception {
        final ResponseEntity<User> responseEntity = createTestUser();
        User user = responseEntity.getBody();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> findByUserName = userController.findByUserName(user.getUsername());
        User userFound = findByUserName.getBody();
        assertEquals(200, findByUserName.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("test", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());
    }

    public ResponseEntity<User> createTestUser() {
        when(encoder.encode("password")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");
        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        return responseEntity;
    }

}
