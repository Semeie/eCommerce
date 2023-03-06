package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
    }
    @Test
    public void test_submit(){
        User testUser = createTestUser();
        UserOrder order = UserOrder.createFromCart(testUser.getCart());
        when(userRepository.findByUsername("test")).thenReturn(testUser);
        when(orderRepository.save(order)).thenReturn(order);
        ResponseEntity<UserOrder> response = orderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals("test", userOrder.getUser().getUsername());
        assertEquals(1, userOrder.getUser().getCart().getItems().size());
    }
    @Test
    public void test_getOrdersForUser(){
        User testUser = createTestUser();
        when(userRepository.findByUsername("test")).thenReturn(testUser);
        ResponseEntity<UserOrder> result = orderController.submit(testUser.getUsername());
        when(orderRepository.findByUser(testUser)).thenReturn(new ArrayList<>(List.of(result.getBody())));
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1,result.getBody().getUser().getId());
        assertEquals(1,response.getBody().stream().count());
    }
    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        Cart cart = new Cart();
        cart.setUser(user);
        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setDescription("best selling");
        item.setPrice(new BigDecimal(29.99));
        cart.addItem(item);
        user.setCart(cart);
        return user;
    }
}
