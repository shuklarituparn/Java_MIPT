package model;

import interfaces.Dispatcher;
import interfaces.Taxi;
import service.TaxiDispatcher;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaxiDriver implements Taxi, Runnable {
    private final Dispatcher dispatcher;
    private final int id;
    private final Lock lock;
    private volatile boolean available;

    public TaxiDriver(int id, Dispatcher dispatcher) {
        this.id = id;
        this.dispatcher = dispatcher;
        this.lock = new ReentrantLock();
        this.available = true;
    }

    @Override
    public void executeOrder(Order order) {
        System.out.println("Taxi " + id + " starting order: " + order.getDetails());
        try {
            Thread.sleep((long) (Math.random() * 50000) + 1000);
            System.out.println("Taxi " + id + " finished order: " + order.getDetails());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        notifyDispatcher();
    }

    @Override
    public void notifyDispatcher() {
        dispatcher.taxiFinished(this);
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailability(boolean available) {
        this.available = available;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                if (available && dispatcher instanceof TaxiDispatcher taxiDispatcher) {
                    Order order = taxiDispatcher.getNextOrder();
                    if (order != null) {
                        available = false;
                        executeOrder(order);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}
