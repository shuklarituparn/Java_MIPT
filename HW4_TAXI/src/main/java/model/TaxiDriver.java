package model;

import interfaces.Dispatcher;
import interfaces.Taxi;
import service.TaxiDispatcher;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaxiDriver implements Taxi, Runnable {
    private final Dispatcher dispatcher;
    private final int id;
    private final AtomicBoolean available;
    private volatile boolean running;

    public TaxiDriver(int id, Dispatcher dispatcher) {
        this.id = id;
        this.dispatcher = dispatcher;
        this.available = new AtomicBoolean(true);
        this.running = true;
    }

    @Override
    public void executeOrder(Order order) {
        System.out.println("Taxi " + id + " starting order: " + order.getDetails());
        try {
            Thread.sleep((long) (Math.random() * 5000) + 1000);
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
        return available.get();
    }

    public void setAvailability(boolean availability) {
        available.set(availability);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (available.get() && dispatcher instanceof TaxiDispatcher taxiDispatcher) {
                    Order order = taxiDispatcher.getNextOrder();
                    if (order != null && available.compareAndSet(true, false)) {
                        executeOrder(order);
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}