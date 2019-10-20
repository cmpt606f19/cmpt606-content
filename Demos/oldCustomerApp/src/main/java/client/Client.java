package client;

import jpa.repository.CustomerJdbcRepository;
import jpa.repository.CustomerRepository;
import jpa.repository.OrderRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import jpa.entity.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Client {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {

        //If True use embedded Derby Driver. If false connect to Derby Server, in this case you must start the server first.
        boolean useEmbeddedDerbyDriver = false;
        int aliCustomerId = 1;

        //<editor-fold defaultstate="collapsed" desc="1. Create some test customers ">
        CustomerRepository customerRepository = new CustomerRepository();
        CustomerJdbcRepository customerJdbcRepository = new CustomerJdbcRepository();
        try {
            System.out.println("1. Create two test customers using JPA - this will also create the database\n");
            emf = Persistence.createEntityManagerFactory("DerbyCustomerDBPeristenceUnit");
            em = emf.createEntityManager();

            Customer customer1 = new Customer("Ali Taleh");
            Customer customer2 = new Customer("Moza Ahmed");

            em.getTransaction().begin();
            em.persist(customer1);
            em.persist(customer2);
            em.getTransaction().commit();
            
            aliCustomerId = customer1.getId();
            System.out.println(String.format("Two customers were created. Their auto-generated Ids are %d & %d\n", customer1.getId(), customer2.getId()));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="2. Get customers using JDBC">
        System.out.println("2. Get customers using JDBC\n");
        try {
            //Load JDBC Driver
            if (useEmbeddedDerbyDriver) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } else {
                Class.forName("org.apache.derby.jdbc.ClientDriver"); //Network driver
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            List<Customer> customers = new ArrayList<Customer>();

            //Create a JDBC Connection
            Connection dbConnection;
            if (useEmbeddedDerbyDriver) {
                dbConnection = DriverManager.getConnection("jdbc:derby:memory:CustomerDB;user=APP;password=APP");
            } else {
                dbConnection = DriverManager.getConnection("jdbc:derby://localhost:1527/CustomerDB;user=APP;password=APP");  //Network database
            }
            
            Statement stmt = dbConnection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                customers.add(new Customer(id, name));
            }

            for (Customer customer : customers) {
                System.out.println(customer.getId() + "   " + customer.getName());
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        //</editor-fold>

        //Comment this to run more scenarios
        //System.exit(0);

        //<editor-fold defaultstate="collapsed" desc="3. Get customers using JPA">
        System.out.println("\n3. Get customer 'Ali Taleh' using JPA then update the name to 'Ali Saleh'\n");
        try {

            System.out.println("Customer details before update:");
            Customer customer = em.find(Customer.class, aliCustomerId);
            System.out.println(customer.getId() + "   " + customer.getName());

            //Update the name
            customer.setName("Ali Saleh");
            
            //Save the change to the database
            em.getTransaction().begin();
            em.merge(customer);
            em.getTransaction().commit();
            
            //Read it again
            System.out.println("\nCustomer details after update:");
            customer = em.find(Customer.class, aliCustomerId);
            System.out.println(customer.getId() + "   " + customer.getName());

        } catch (Exception e) {
            System.err.println(e);
        }
        //</editor-fold>

        //Comment this to run more scenarios
        //System.exit(0);

        //<editor-fold defaultstate="collapsed" desc="4. Select customers using JPQL Query">
        System.out.println("\n4. Customers list using JPQL:");
        Query query = em.createQuery("select c from Customer as c");
        List<Customer> customers = query.getResultList();
        for (Customer cust : customers) {
            System.out.println(cust.getId() + "   " + cust.getName());
        }

        //</editor-fold>

        //Comment this to run more scenarios
        //System.exit(0);

        //<editor-fold defaultstate="collapsed" desc="5. Create a new Customer then delete it">
        System.out.println("\n5. Create a new Customer then delete it");
        Customer customer3 = new Customer("Shrek Funny");
        em.getTransaction().begin();
        em.persist(customer3);
        em.getTransaction().commit();

        int idOfNewRecord = customer3.getId();

        //Read it again
        System.out.println("Get newly added customer:");
        Customer customer = em.find(Customer.class, idOfNewRecord);
        System.out.println(customer.getId() + "   " + customer.getName());

        //Delete it
        System.out.println(String.format("Delete customer: %s  %s", customer.getId(), customer.getName()));
        em.getTransaction().begin();
        em.remove(customer);
        em.getTransaction().commit();

        customer = em.find(Customer.class, idOfNewRecord);

        if (customer == null) {
            System.out.println(String.format("Customer with Id %d was successfully deleted", idOfNewRecord));
        }
        //</editor-fold>

        //Comment this to run more scenarios
        //System.exit(0);

        //<editor-fold defaultstate="collapsed" desc="6. Insert Orders...">
        System.out.println("\n6. Insert Orders...\n");
        System.out.println("Inserting 2 orders for Customer with Id 1");
        
        customer = em.find(Customer.class, 1);
        
        // Create 2 orders
        Order order1 = new Order();
        order1.setAddress("123 University Rd, Doha, Qatar");
        order1.setQty(10);
        customer.addOrder(order1);
        
        Order order2 = new Order();
        order2.setAddress("567 1st St. Zaid Rd, Dubai, UAE");
        order2.setQty(20);
        customer.addOrder(order2);
        
        
        em.getTransaction().begin();
        em.merge(customer);
        em.getTransaction().commit();
        
        
        System.out.println("Inserting 2 orders for Customer with Id 2");
        customer = em.find(Customer.class, 2);
        
        // Create 2 orders
        Order order3 = new Order();
        order3.setAddress("123 University Rd, Doha, Qatar");
        order3.setQty(200);
        customer.addOrder(order3);

        Order order4 = new Order();
        order4.setAddress("567 1st St. Zaid Rd, Dubai, UAE");
        order4.setQty(100);
        customer.addOrder(order4);
        
      
        em.getTransaction().begin();
        em.merge(customer);
        em.getTransaction().commit();


        System.out.println("\nOrders Summary using JDBC:");
        List<OrderSummary> orderSummaryList = customerJdbcRepository.GetOrdersSummary();
        for (OrderSummary orderSummary : orderSummaryList) {
            System.out.println(orderSummary.geCustomertName() + " \t " + orderSummary.getTotalQty());
        }

        System.out.println("\nOrders Summary using JPA:");
        orderSummaryList = customerRepository.GetOrdersSummary();
        for (OrderSummary orderSummary : orderSummaryList) {
            System.out.println(orderSummary.geCustomertName() + " \t " + orderSummary.getTotalQty());
        }

        System.out.println("\nOrders Summary using JDBC for CustomerID 1:");
        OrderSummary orderSummary = customerJdbcRepository.GetOrdersSummary(1);
        if (orderSummary != null)
            System.out.println(orderSummary.geCustomertName() + " \t Qty: " + orderSummary.getTotalQty());
        else
            System.out.println("No order found.");

        System.out.println("\nOrders Summary using JPA for CustomerID 1:");
        orderSummary = customerJdbcRepository.GetOrdersSummary(1);
        System.out.println(orderSummary.geCustomertName() + " \t Qty: " + orderSummary.getTotalQty());

        System.out.println("\nDelete Order with Id 1.");
        Order orderToDelete = em.find(Order.class, 1);
        em.getTransaction().begin();
        em.remove(orderToDelete);
        em.getTransaction().commit();
        
        System.out.println("\nOrders for CustomerID 1 using JPA (note that only one order left):");
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = orderRepository.getOrdersByCustomerId(1);
        for (Order order : orders) {
            System.out.println("OrderId#" + order.getId() + "\t - Ship to: '" +order.getAddress() + "' \t Qty:  " + order.getQty());
        }


        //</editor-fold>

    }

    private static boolean verifyInsert() {

        CustomerRepository customerRepository = new CustomerRepository();
        Customer c = customerRepository.getCustomer("Ahmed Munir");

        Collection<Order> orders = c.getOrders();
        if (orders == null || orders.size() != 2) {
            throw new RuntimeException("Unexpected number of orders: " + ((orders == null) ? "null" : "" + orders.size()));
        }

        return true;
    }
}
