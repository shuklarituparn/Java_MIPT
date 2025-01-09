package service;
import model.Order;
import model.TaxiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TaxiDispatcherTest {
    private TaxiDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new TaxiDispatcher();
    }

    @AfterEach
    void tearDown() {
        dispatcher.shutdown();
    }

    @Test
    void testOrderProcessing() throws InterruptedException {
        int numberOfTaxis = 3;
        for (int i = 1; i <= numberOfTaxis; i++) {
            TaxiDriver taxi = new TaxiDriver(i, dispatcher);
            dispatcher.addTaxi(taxi);
        }

        int numberOfOrders = 5;
        for (int i = 1; i <= numberOfOrders; i++) {
            dispatcher.placeOrder(new Order("Test Order " + i));
        }

        dispatcher.startDispatch();

        long startTime = System.currentTimeMillis();
        while (dispatcher.hasActiveOrders() &&
                (System.currentTimeMillis() - startTime) < 30000) {
            Thread.sleep(100);
        }

        assertFalse(dispatcher.hasActiveOrders(), "All orders should be processed");
    }

    @Test
    void testConcurrentOrderAddition() throws InterruptedException {
        for (int i = 1; i <= 2; i++) {
            dispatcher.addTaxi(new TaxiDriver(i, dispatcher));
        }

        dispatcher.startDispatch();
        int numberOfThreads = 3;
        int ordersPerThread = 5;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    for (int j = 1; j <= ordersPerThread; j++) {
                        dispatcher.placeOrder(new Order(
                                "Thread " + threadId + " Order " + j));
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        assertTrue(latch.await(30, TimeUnit.SECONDS),
                "Timeout waiting for orders to be added");

        long startTime = System.currentTimeMillis();
        while (dispatcher.hasActiveOrders() &&
                (System.currentTimeMillis() - startTime) < 30000) {
            Thread.sleep(100);
        }

        assertFalse(dispatcher.hasActiveOrders(),
                "All concurrent orders should be processed");
    }
}