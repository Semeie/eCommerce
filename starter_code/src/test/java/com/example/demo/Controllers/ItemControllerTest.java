package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository= mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }
    @Test
    public void test_get_item_by_id(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createTestItem()));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("book", response.getBody().getName());
    }
    @Test
    public void test_get_items_by_name() {
        when(itemRepository.findByName("book")).thenReturn(new ArrayList<>(List.of(createTestItem())));

        ResponseEntity<List<Item>> items = itemController.getItemsByName("book");

        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals("book", items.getBody().get(0).getName());
        assertEquals(1,items.getBody().size());
    }
    @Test
    public void test_get_items() {
        when(itemRepository.findByName("book")).thenReturn(new ArrayList<>(List.of(createTestItem())));

        ResponseEntity<List<Item>> items = itemController.getItemsByName("book");

        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals("book", items.getBody().get(0).getName());
        assertEquals(1,items.getBody().size());
    }


    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setDescription("best selling");
        item.setPrice(new BigDecimal(29.99));
        return item;
    }

}
