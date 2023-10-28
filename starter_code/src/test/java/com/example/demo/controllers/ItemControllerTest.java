package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }
    @Test
    public void getAllItems() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(createItem(1L), createItem(2L)));
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }
    @Test
    public void getItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1L)));
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("Name #1", item.getName());
    }
    @Test
    public void getItemById_NotFound() {
        final ResponseEntity<Item> response = itemController.getItemById(500L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName() {
        Item item1 = createItem(1L);
        when(itemRepository.findByName(item1.getName())).thenReturn(Arrays.asList(item1));
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Name #1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }
    public static Item createItem(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(5.67));
        item.setName("Name #" + id);
        item.setDescription("Description #" + id);
        return item;
    }
}
