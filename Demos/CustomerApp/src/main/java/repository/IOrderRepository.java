package repository;

import java.util.List;
import entity.Order;

public interface IOrderRepository {
    Order add(Order order);
    void update(Order order);
    void delete(int id);
    List<Order> getOrders();
    Order getOrder(int id);
    int getOrdersCount();
    List<Order> getOrdersByCustomerId(int customerId);
}
