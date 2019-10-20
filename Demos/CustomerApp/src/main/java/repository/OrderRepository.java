package repository;

import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import entity.Order;

@ApplicationScoped
public class OrderRepository implements IOrderRepository {

    //@PersistenceContext
    //private EntityManager em;

    // This is only needed when using JDBC
    //@Resource(mappedName = "java:app/jdbc/ContactDB")
    //private DataSource dataSource;
    
    private EntityManagerFactory emf = null;
    private EntityManager em;

    public OrderRepository() {
        emf = Persistence.createEntityManagerFactory("DBPeristenceUnit");
        em = emf.createEntityManager();
    }

    public Order add(Order order) {
    	em.getTransaction().begin();
    	em.persist(order);
        em.getTransaction().commit();
        return order;
    }

    public void update(Order order) {
    	em.getTransaction().begin();
        em.merge(order);
        em.getTransaction().commit();
    }

    public void delete(int id) {
        Order order = em.getReference(Order.class, id);
        em.getTransaction().begin();
        em.remove(order);
        em.getTransaction().commit();
    }

    public List<Order> getOrders() {
        Query query = em.createQuery("select o from Order as o");
        return query.getResultList();
    }

    public Order getOrder(int id) {
        return em.find(Order.class, id);
    }

    public int getOrdersCount() {
        return ((Long) em.createQuery("select count(o) from Order as o")
                .getSingleResult()).intValue();
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        Query query = em.createNamedQuery("Order.findOrdersByCustomerId");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
}