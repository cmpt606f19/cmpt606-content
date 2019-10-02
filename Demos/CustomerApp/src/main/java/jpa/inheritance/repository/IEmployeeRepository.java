package jpa.inheritance.repository;

import java.util.List;

import jpa.inheritance.JoinedTable.Employee;

public interface IEmployeeRepository {

    public abstract void add(Employee employee);

    public abstract void update(Employee employee);

    public abstract void delete(int id);

    public abstract List<Employee> getEmployees();

    public abstract Employee getEmployee(int id);

}
