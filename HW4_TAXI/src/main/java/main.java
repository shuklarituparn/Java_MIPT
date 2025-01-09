import model.Order;
import model.TaxiDriver;
import service.TaxiDispatcher;

public class main {
    public static void main(String[] args) {
        TaxiDispatcher dispatcher = new TaxiDispatcher();

        int numberOfTaxis = 5;
        for (int i = 1; i <= numberOfTaxis; i++) {
            TaxiDriver taxi = new TaxiDriver(i, dispatcher);
            dispatcher.addTaxi(taxi);
        }

        for (int i = 1; i <= 10; i++) {
            Order order = new Order("Order " + i);
            dispatcher.placeOrder(order);
        }

        dispatcher.startDispatch();
    }
}