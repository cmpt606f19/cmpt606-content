package jpa.repository;

import java.sql.SQLException;
import java.util.List;
import jpa.entity.Customer;
import jpa.entity.OrderSummary;

public interface ICustomerRepository {

    public abstract Customer getCustomer(int customerId);

    public abstract Customer addCustomer(Customer customer);

    public abstract void updateCustomer(Customer customer);

    public abstract void deleteCustomer(int id);

    public abstract List<Customer> getCustomers();

    public abstract Customer getCustomer(String customerName);

    public abstract int getCustomersCount();

    public abstract List<OrderSummary> GetOrdersSummary();

    public abstract OrderSummary getOrdersSummary(int customerId);
    
    public void insertTestData();
}
