package jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ORDERS")
@NamedQueries({
    @NamedQuery(name = "Order.findOrdersByCustomerId",
            query = "SELECT o FROM Order o WHERE o.customerId = :customerId")})

@XmlRootElement
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private int id;

    @Column(name = "SHIPPING_ADDRESS")
    private String address;

    private int qty;
    
    private int customerId;

    //This is removed because json serializer cannot handle it
    //@ManyToOne()
    //@JoinColumn(name = "CUSTOMER_ID")
    //private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    

    /*
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    */

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
