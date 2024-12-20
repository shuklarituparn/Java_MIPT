package interfaces;

import model.Order;

public interface Taxi {
    void executeOrder(Order order);
    void notifyDispatcher();
    int getId();
}