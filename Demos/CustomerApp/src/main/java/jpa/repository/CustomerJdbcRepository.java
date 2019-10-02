package jpa.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import jpa.entity.Customer;
import jpa.entity.OrderSummary;

@ApplicationScoped
public class CustomerJdbcRepository { //implements ICustomerRepository {
    //This is only needed when using JDBC
    //@Resource(mappedName = "java:app/jdbc/ContactDB")
    //private DataSource dataSource;
    private boolean useEmbeddedDerbyDriver = false;
    private Connection getDBConnection() throws Exception {
        Connection dbConnection;
        try {
            //Load JDBC Driver
            if (useEmbeddedDerbyDriver) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } else {
                Class.forName("org.apache.derby.jdbc.ClientDriver"); //Network driver
            }

            //Create a JDBC Connection

            if (useEmbeddedDerbyDriver) {
                dbConnection = DriverManager.getConnection("jdbc:derby:memory:CustomerDB;user=APP;password=APP");
            } else {
                dbConnection = DriverManager.getConnection("jdbc:derby://localhost:1527/CustomerDB;user=APP;password=APP");  //Network database
            }
        } catch (Exception e) {
            throw e;
        }
        return dbConnection;
    }
    
        //getDBConnection()
    public Customer addCustomer(Customer customer) throws SQLException, Exception {
        String insertQuery = "insert into Customer(customerName) values("
                + customer.getName() + ")";
        try (Connection dbConnection = getDBConnection();
                Statement stmt = dbConnection.createStatement()) {

            int customerId = stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            customer.setId(customerId);
        }
        return customer;
    }

    public int updateCustomer(Customer customer) throws SQLException, Exception {
        String updateQuery = "Update Customer set Name = '"
                + customer.getName() + "' where id = "
                + customer.getId();

        System.out.println(updateQuery);
        int numberOfAffectedRows = 0;
        try (Connection dbConnection = getDBConnection();
                Statement stmt = dbConnection.createStatement()) {

            numberOfAffectedRows = stmt.executeUpdate(updateQuery);
        }
        return numberOfAffectedRows;
    }

    public int deleteCustomer(int id) throws SQLException, Exception {
        String deleteQuery = "Delete from Customer where id = " + id;
        int numberOfAffectedRows = 0;
        try (Connection dbConnection = getDBConnection();
                Statement stmt = dbConnection.createStatement()) {

            numberOfAffectedRows = stmt.executeUpdate(deleteQuery);
        } 
        return numberOfAffectedRows;
    }

    public List<Customer> getCustomers() throws SQLException, Exception {
        List<Customer> customers = new ArrayList<>();

        try (Connection dbConnection = getDBConnection();
                Statement stmt = dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                customers.add(new Customer(id, name));
            }
        }
        return customers;
    }

    public Customer getCustomer(int id) throws SQLException, Exception {
        Customer customer = null;
        try (Connection dbConnection = getDBConnection();
                Statement stmt = dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Customer where id = " + id)) {

            while (rs.next()) {
                customer = new Customer(rs.getInt("Id"), rs.getString("Name"));
                break;
            }
        }
        return customer;
    }

    public List<OrderSummary> GetOrdersSummary() {

        List<OrderSummary> orderSummaryList = new ArrayList<>();

        try (Connection con = getDBConnection();
                Statement stmt = con.createStatement()) {

            // Create and execute an SQL statement that returns a
            // set of data and then display it.
            String SQL = "Select C.NAME CustomerName, SUM(O.Qty) TotalQty from CUSTOMER C"
                    + " join ORDERS O on C.ID = O.CUSTOMER_ID"
                    + " Group by C.NAME";
            
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                OrderSummary orderSummary = new OrderSummary(
                        rs.getString("CustomerName"),
                        (Long) rs.getLong("TotalQty"));
                orderSummaryList.add(orderSummary);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderSummaryList;
    }

    public OrderSummary GetOrdersSummary(int customerId) {
        OrderSummary orderSummary = null;
        String SQL = "Select C.NAME CustomerName, SUM(O.Qty) TotalQty from CUSTOMER C"
                + " join ORDERS O on C.ID = O.CUSTOMER_ID "
                + " Where C.Id = ?"
                + " Group by C.NAME";

        try (Connection con = getDBConnection();
                PreparedStatement stmt = con.prepareStatement(SQL)) {

            // Create and execute an SQL statement that returns a
            // set of data and then display it.
            stmt.setInt(1, customerId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orderSummary = new OrderSummary(
                        rs.getString("CustomerName"),
                        (Long) rs.getLong("TotalQty"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderSummary;
    }
}
