package service;

import interfaces.Dispatcher;
import interfaces.Taxi;
import model.Order;
import model.TaxiDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaxiDispatcher implements Dispatcher {
    private final List<Taxi> taxis;
    private final Queue<Order> orderQueue;
    private final Lock lock;

    public TaxiDispatcher() {
        this.taxis = new ArrayList<>();
        this.orderQueue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
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
        for (Taxi taxi : taxis) {
            Thread taxiThread = new Thread((Runnable) taxi);
            taxiThread.start();
        }
    }
}
