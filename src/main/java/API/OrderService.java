package API;

import DTO.Order;
import Events.OrderEvent;

import java.io.IOException;

public interface OrderService {

    void publishEvent(OrderEvent event) throws IOException;

    Order findOrder(int id);
}
