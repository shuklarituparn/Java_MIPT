package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import service.TaxiDispatcher;
import static org.junit.jupiter.api.Assertions.*;

class TaxiDriverTest {
    private TaxiDriver taxiDriver;
    private TaxiDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new TaxiDispatcher();
        taxiDriver = new TaxiDriver(1, dispatcher);
    }

    @Test
    void testInitialState() {
        assertTrue(taxiDriver.isAvailable());
        assertEquals(1, taxiDriver.getId());
    }

    @Test
    void testAvailabilityToggle() {
        assertTrue(taxiDriver.isAvailable());
        taxiDriver.setAvailability(false);
        assertFalse(taxiDriver.isAvailable());
    }

    @Test
    void testOrderExecution() throws InterruptedException {
        Order order = new Order("Test Order");
        taxiDriver.setAvailability(false);

        Thread orderThread = new Thread(() -> taxiDriver.executeOrder(order));
        orderThread.start();

        orderThread.join(10000);

        assertTrue(taxiDriver.isAvailable());
    }
}