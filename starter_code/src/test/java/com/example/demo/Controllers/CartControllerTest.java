package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController,"userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }
    @Test
    public void test_add_to_cart() {
        when(userRepository.findByUsername("test")).thenReturn(createTestUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createTestItem()));
        ResponseEntity<Cart> response = cartController.addTocart(testModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(3, cart.getItems().size());
        assertEquals("test", cart.getUser().getUsername());
    }
    @Test
    public void test_addTocart_user_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addTocart(testModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void test_addTocart_item_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(createTestUser());
        ResponseEntity<Cart> response = cartController.addTocart(testModifyCartRequest());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void test_remove_to_cart() {
        when(userRepository.findByUsername("test")).thenReturn(createTestUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createTestItem()));
        ResponseEntity<Cart> response = cartController.removeFromcart(testModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
        assertEquals("test", cart.getUser().getUsername());
    }
    @Test
    public void test_removeFromcart_user_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(testModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void test_removeFromcart_item_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(createTestUser());
        ResponseEntity<Cart> response = cartController.removeFromcart(testModifyCartRequest());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setDescription("best selling");
        item.setPrice(new BigDecimal(29.99));
        return item;
    }
    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }
    private ModifyCartRequest testModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername("test");

        return modifyCartRequest;
    }
}
