package repository;

import java.util.List;

import entity.Customer;
import entity.OrderSummary;

public interface ICustomerRepository {
	Customer getCustomer(int customerId);
	Customer addCustomer(Customer customer);
	void updateCustomer(Customer customer);
	void deleteCustomer(int id);
	List<Customer> getCustomers();
	Customer getCustomer(String customerName);
	int getCustomersCount();
	List<OrderSummary> GetOrdersSummary();
	OrderSummary getOrdersSummary(int customerId);
	void insertTestData();
}