package repository;

import java.util.List;

import entity.Customer;
import entity.OrderSummary;

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
