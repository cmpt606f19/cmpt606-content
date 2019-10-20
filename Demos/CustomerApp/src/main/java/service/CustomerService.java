package service;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entity.Customer;
import entity.Order;
import entity.OrderSummary;
import repository.ICustomerRepository;
import repository.IOrderRepository;

@Path("/api/customers")
@ApplicationScoped
public class CustomerService {         
    @Inject
    ICustomerRepository customerRepository;
    
    @Inject
    IOrderRepository orderRepository;
    
    //You can test it using Postman App - http://www.getpostman.com/
    //Url: http://localhost:8080/api/customers using Get request
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers() {
    	return customerRepository.getCustomers();
    }

   
    //You can test it using Postman App - http://www.getpostman.com/
    //Url: http://localhost:8080/api/customers/1 using Get request
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") int customerId) {
        Customer customer = customerRepository.getCustomer(customerId);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            String msg = String.format("Customer # %d not found", customerId);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
    }
    
    @GET
    @Path("orders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderSummary> getOrders() {
        return customerRepository.GetOrdersSummary();
    }
    
    @GET
    @Path("{customerId}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public OrderSummary getOrders(@PathParam("customerId") int customerId) {
        return customerRepository.getOrdersSummary(customerId);
    }

    /*You can test it using Postman App - http://www.getpostman.com/
     Url: http://localhost:8080/api/customers using Post request
    */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addCustomer(Customer customer) {
        try {
            customer = customerRepository.addCustomer(customer);
            String location = String.format("/api/customers/%s", customer.getId());
            String msg = String.format("customer #%d created successfully", customer.getId());
            return Response.created(new URI(location)).entity(msg).build();
        } catch (Exception ex) {
            String msg = String.format("Adding customer failed because : %s",
                    ex.getCause().getMessage());
            return Response.serverError().entity(msg).build();
        }
    }

    /*You can test it using Postman App - http://www.getpostman.com/
     Url: http://localhost:8080/api/customers using Put request
    */
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateCustomer(@PathParam("id") int customerId, Customer customer) {
        customer.setId(customerId);
        try {
            customerRepository.updateCustomer(customer);
            String msg = String.format("Customer #%s updated sucessfully", customer.getId());
            return Response.ok(msg).build();
        } catch (Exception ex) {
            String msg = String.format("Updating customer failed because : \n%s",
                    ex.getMessage());
            return Response.serverError().entity(msg).build();
        }
    }

    /*You can test it using Postman App - http://www.getpostman.com/
     Url: http://localhost:8080/api/customers/1 using Delete request
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int customerId) {
        try {
            customerRepository.deleteCustomer(customerId);
            String msg = String.format("Customer #%s deleted sucessfully", customerId);
            return Response.ok(msg).build();
        } catch (Exception ex) {
            String msg = String.format("Deleting customer failed because : %s",
                    ex.getCause().getMessage());
            return Response.serverError().entity(msg).build();
        }
    }

    /*You can test it using Postman App - http://www.getpostman.com/
     Url: http://localhost:8080/api/customers/{id}/orders using Post request
    */
    @POST
    @Path("/{id}/orders")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addOrder(@PathParam("id") int customerId, Order order) {
        try {
            order = orderRepository.add(order);
            String location = String.format("/jpa/%d/orders/%s", customerId, order.getId());
            String msg = String.format("order #%d created successfully", order.getId());
            return Response.created(new URI(location)).entity(msg).build();
        } catch (Exception ex) {
            String msg = String.format("Adding order failed because : %s",
                    ex.getCause().getMessage());
            return Response.serverError().entity(msg).build();
        }
    }

    @GET
    @Path("/{customerId}/orders/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        return orderRepository.getOrder(orderId);
    }
    
    /*You can test it using Postman App - http://www.getpostman.com/
     Url: http://localhost:8080/api/customers/{id}/orders using Post request
    */
    @DELETE
    @Path("/{customerId}/orders/{orderId}")
    public Response deleteOrder(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        try {
            orderRepository.delete(orderId);
            String msg = String.format("order #%s deleted sucessfully", orderId);
            return Response.ok(msg).build();
        } catch (Exception ex) {
            String msg = String.format("Deleting order failed because : %s",
                    ex.getCause().getMessage());
            return Response.serverError().entity(msg).build();
        }
    }
}