package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void testOrderCreation() {
        String details = "Test Order";
        Order order = new Order(details);
        assertEquals(details, order.getDetails());
    }
}