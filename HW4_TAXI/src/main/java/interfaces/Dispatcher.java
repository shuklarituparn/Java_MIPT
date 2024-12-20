package interfaces;

import model.Order;
import interfaces.Taxi;

public interface Dispatcher {
    void placeOrder(Order order);
    void taxiFinished(Taxi taxi);
}
