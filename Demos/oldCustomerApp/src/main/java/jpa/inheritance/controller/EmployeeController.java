/*
package jpa.inheritance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpa.inheritance.JoinedTable.Employee;
import jpa.inheritance.JoinedTable.FullTimeEmployee;
import jpa.inheritance.JoinedTable.PartTimeEmployee;
import jpa.inheritance.repository.IEmployeeRepository;

@WebServlet("/employee")
public class EmployeeController extends HttpServlet {

    @Inject
    IEmployeeRepository employeeRepository;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        List<String> outputLines = new ArrayList<>();
        outputLines.add("<br><a href='customer'>JPA Entity Relations Example</a>");

        outputLines.add("<h3>Employee created using PartTimeEmployee & FullTimeEmployee classes. Change the Controller and exprience using PartTimeEmployee2, FullTimeEmployee2, PartTimeEmployee3 & FullTimeEmployee3 classes. Check ContactDB database to see how the tables are created.</h3>\n");

        PartTimeEmployee partTimeEmployee = new PartTimeEmployee();
        partTimeEmployee.setName("Ahmed");
        partTimeEmployee.setStartDate(new java.util.Date());
        partTimeEmployee.setHourlyRate(50);
        employeeRepository.add(partTimeEmployee);

        FullTimeEmployee fullTimeEmployee = new FullTimeEmployee();
        fullTimeEmployee.setName("Layla");
        fullTimeEmployee.setStartDate(new java.util.Date());
        fullTimeEmployee.setSalary(50000);
        fullTimeEmployee.setOfficeNumber("G110");
        employeeRepository.add(fullTimeEmployee);

        List<Employee> employees = employeeRepository.getEmployees();

        for (Employee employee : employees) {
            outputLines.add(String.format("Employee Id %d - %s", employee.getId(), employee.getName()));
        }

        displayOutput(response, outputLines);
    }

    private void displayOutput(HttpServletResponse response,
            List<String> outputLines) {
        try {
            response.setContentType("text/html");
            PrintWriter outToBrowser = response.getWriter();
            for (String line : outputLines) {
                outToBrowser.println(line);
                outToBrowser.println("<br>");
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/