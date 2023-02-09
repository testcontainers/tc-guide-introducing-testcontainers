package com.testcontainers.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public CustomerService() {
        createCustomersTableIfNotExists();
    }

    public void createCustomer(Customer customer) {
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("insert into customers(id,name) values(?,?)");
            pstmt.setLong(1, customer.getId());
            pstmt.setString(2, customer.getName());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select id,name from customers");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                customers.add(new Customer(id,name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    private void createCustomersTableIfNotExists() {
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                """
                    create table if not exists customers (
                        id bigint not null,
                        name varchar not null,
                        primary key (id)
                    )
                    """);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        try {
            String jdbcUrl = System.getProperty("JDBC_URL");
            String jdbcUsername = System.getProperty("JDBC_USERNAME");
            String jdbcPassword = System.getProperty("JDBC_PASSWORD");
            return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
