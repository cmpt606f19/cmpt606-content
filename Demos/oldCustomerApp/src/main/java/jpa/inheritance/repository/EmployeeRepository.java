package jpa.inheritance.repository;

import java.util.List;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inheritance.JoinedTable.Employee;

//@Stateless
public class EmployeeRepository implements IEmployeeRepository {

    //@PersistenceContext
    private EntityManager em;

    public void add(Employee employee) {
        em.persist(employee);
    }

    public void update(Employee employee) {
        em.merge(employee);
    }

    public void delete(int id) {
        Employee employee = em.getReference(Employee.class, id);
        em.remove(employee);
    }

    public List<Employee> getEmployees() {
        Query query = em.createQuery("select e from Employee as e");
        return query.getResultList();
    }

    public Employee getEmployee(int id) {
        return em.find(Employee.class, id);
    }

}
