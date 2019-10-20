package repository;

import java.util.List;
import javax.annotation.PostConstruct;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import entity.Customer;
import entity.Order;
import entity.OrderSummary;

@ApplicationScoped
public class CustomerRepository implements ICustomerRepository {

    //@PersistenceContext
    //private EntityManager em;
	
    
    private EntityManagerFactory emf = null;
    private EntityManager em;

    public CustomerRepository() {
        emf = Persistence.createEntityManagerFactory("DBPeristenceUnit");
        em = emf.createEntityManager();
    }

    //This is only needed when using JDBC
    //@Resource(mappedName = "java:app/jdbc/ContactDB")
    //private DataSource dataSource;

    public Customer getCustomer(int customerId) {
        return em.find(Customer.class, customerId);
    }

    //@Transactional
    public Customer addCustomer(Customer customer) {
    	
        em.getTransaction().begin();
        em.persist(customer);
        //em.flush();
        em.getTransaction().commit();
        return customer;
    }

    public void updateCustomer(Customer customer) {
    	em.getTransaction().begin();
        em.merge(customer);
        em.getTransaction().commit();
    }

    public void deleteCustomer(int id) {
    	em.getTransaction().begin();
        Query query = em.createQuery("delete from Customer c WHERE c.id = :id");
        int deletedCount = query.setParameter("id", id).executeUpdate();
        em.getTransaction().commit();
    }

    public List<Customer> getCustomers() {
        return em.createQuery("select c from Customer as c").getResultList();
    }

    public Customer getCustomer(String customerName) {
        Query query = em.createQuery("select c from Customer c where c.name = :custName");
        query.setParameter("custName", customerName);
        return (Customer) query.getSingleResult();
    }

    public int getCustomersCount() {
        return ((Long) em.createQuery("select count(c) from Customer as c").getSingleResult()).intValue();
    }

    public List<OrderSummary> GetOrdersSummary() {
        Query query = em.createNamedQuery("Customer.GetOrdersSummary");
        return query.getResultList();
    }

    public OrderSummary getOrdersSummary(int customerId) {
        Query query = em.createNamedQuery("Customer.GetOrdersSummaryByCustomerId");
        query.setParameter("customerId", customerId);
        return (OrderSummary) query.getSingleResult();
    }
    
    @PostConstruct
    public void insertTestData() {
        Customer customer1 = new Customer("Ali Taleh");
        Customer customer2 = new Customer("Moza Ahmed");

        customer1 = this.addCustomer(customer1);
        customer2 = this.addCustomer(customer2);

        System.out.println(String.format("Two customers were created. Their auto-generated Ids are %d & %d\n",
            customer1.getId(), customer2.getId()));
                
        System.out.println("Inserting 2 orders for Customer with Id 1");
        // Create 2 orders
        Order order1 = new Order();
        order1.setAddress("123 University Rd, Doha, Qatar");
        order1.setQty(10);
        customer1.addOrder(order1);

        Order order2 = new Order();
        order2.setAddress("567 Al-Majd Rd, Dukhan, Qatar");
        order2.setQty(20);
        customer1.addOrder(order2);
        
        System.out.println("Inserting 2 orders for Customer with Id 2");
        // Create 2 orders
        Order order3 = new Order();
        order3.setAddress("123 University Rd, Doha, Qatar");
        order3.setQty(200);
        customer2.addOrder(order3);

        Order order4 = new Order();
        order4.setAddress("567 Al-Majd Rd, Dukhan, Qatar");
        order4.setQty(100);
        customer2.addOrder(order4);

        this.updateCustomer(customer1);
        this.updateCustomer(customer2);
    }
}