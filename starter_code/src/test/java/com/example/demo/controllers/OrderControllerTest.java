package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        User user = initUserWithCart();
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        when(userRepository.findByUsername("Atheer")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(Arrays.asList(userOrder));
    }
    @Test
    public void submitCart() {
        final ResponseEntity<UserOrder> response = orderController.submit("Atheer");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals("Atheer", userOrder.getUser().getUsername());
        assertEquals(2, userOrder.getItems().size());
    }

    @Test
    public void submit_userNotFound() {
        final ResponseEntity<UserOrder> response = orderController.submit("false_username");
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void getOrdersForUser_success() {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Atheer");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
        assertEquals(1, userOrders.size());
    }
    public static User initUserWithCart(){
        User user = createUser();
        Cart cart = new Cart();
        Item item1 = createItem(1L);
        Item item2 = createItem(2L);
        cart.setUser(user);
        cart.addItem(item1);
        cart.addItem(item2);
        user.setCart(cart);
        return user;
    }
    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Atheer");
        user.setPassword("AtheerPassword");
        return user;
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
