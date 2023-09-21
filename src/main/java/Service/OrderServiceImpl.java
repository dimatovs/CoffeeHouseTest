package Service;

import API.OrderService;
import DTO.Order;
import Database.DBConnector;
import Events.*;

import java.util.LinkedList;

public class OrderServiceImpl implements OrderService {

    DBConnector dbconnector;

    public void publishEvent(OrderEvent event) {
        dbconnector = new DBConnector();
        boolean isOrderRegistered = dbconnector.isRegistered(event) || event instanceof OrderRegistered;
        boolean isOrderClosed = dbconnector.isCanceledOrIssued(event);
        if (isOrderRegistered && !isOrderClosed) {
                dbconnector.publishEvent(event);
            }
        }

    public Order findOrder(int id) {
        dbconnector = new DBConnector();
        LinkedList<OrderEvent> listOfEvents = dbconnector.getAllEventsByOrderId(id);
        Order order = new Order();
        order.apply(listOfEvents);
        return order;
    }
}
