package service;

import interfaces.Dispatcher;
import interfaces.Taxi;
import model.Order;
import model.TaxiDriver;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaxiDispatcher implements Dispatcher {
    private final List<Taxi> taxis;
    private final Queue<Order> orderQueue;
    private ExecutorService executorService;

    public TaxiDispatcher() {
        this.taxis = new CopyOnWriteArrayList<>();
        this.orderQueue = new ConcurrentLinkedQueue<>();
    }

    public void addTaxi(Taxi taxi) {
        taxis.add(taxi);
    }

    @Override
    public void placeOrder(Order order) {
        orderQueue.add(order);
    }

    public Order getNextOrder() {
        return orderQueue.poll();
    }

    @Override
    public void taxiFinished(Taxi taxi) {
        if (taxi instanceof TaxiDriver driver) {
            driver.setAvailability(true);
        }
    }

    public void startDispatch() {
        executorService = Executors.newFixedThreadPool(taxis.size());
        for (Taxi taxi : taxis) {
            executorService.submit((Runnable) taxi);
        }
    }

    public void shutdown() {
        if (executorService != null) {
            taxis.forEach(taxi -> {
                if (taxi instanceof TaxiDriver driver) {
                    driver.stop();
                }
            });
            executorService.shutdown();
        }
    }

    public boolean hasActiveOrders() {
        return !orderQueue.isEmpty() || taxis.stream()
                .filter(taxi -> taxi instanceof TaxiDriver)
                .map(taxi -> (TaxiDriver) taxi)
                .anyMatch(driver -> !driver.isAvailable());
    }
}