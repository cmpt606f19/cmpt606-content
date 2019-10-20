package jpa.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@NamedQueries({
    @NamedQuery(name = "Customer.GetOrdersSummary",
            query = "select new jpa.entity.OrderSummary(C.name, SUM(O.qty)) "
            + "from Customer C inner join  C.orders O Group by C.name"),

    // Named query with a parameter named customerId
    @NamedQuery(name = "Customer.GetOrdersSummaryByCustomerId",
            query = "select new jpa.entity.OrderSummary(C.name, SUM(O.qty)) "
            + "from Customer C inner join C.orders O "
            + "where c.id = :customerId "
            + "Group by C.name")
})

@Entity
@XmlRootElement
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL) //, mappedBy = "customer"
    @JoinColumn(name = "CUSTOMER_ID")
    private Collection<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name) {
        this.setName(name);
    }

    public Customer(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> newValue) {
        this.orders = newValue;
    }

    public void addOrder(Order order) {
        // **** The association must be set on both sides of the relationship ****
        // Associate orders with the customer so that the orders will be persisted when 
        // the transaction commits
        orders.add(order);

        //Very important to set the Cutomer of the Order otherwise the Order.CustomerId will not be null
        // The association must be set on on the order side because it is the owning side (it has the foreign key).
        order.setCustomerId(this.getId());
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
