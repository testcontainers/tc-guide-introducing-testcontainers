package com.testcontainers.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

class CustomerServiceTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    CustomerService customerService;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        System.setProperty("JDBC_URL", postgres.getJdbcUrl());
        System.setProperty("JDBC_USERNAME", postgres.getUsername());
        System.setProperty("JDBC_PASSWORD", postgres.getPassword());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }

    @Test
    void shouldGetCustomers() {
        customerService.createCustomer(new Customer(1L, "George"));
        customerService.createCustomer(new Customer(2L, "John"));

        List<Customer> customers = customerService.getAllCustomers();
        Assertions.assertEquals(2, customers.size());
    }

}
