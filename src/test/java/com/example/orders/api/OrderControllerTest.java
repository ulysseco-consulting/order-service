package com.example.orders.api;

import com.example.orders.TestDataFactory;
import com.example.orders.domain.Customer;
import com.example.orders.domain.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory data;

    @AfterEach
    void cleanUp() {
        data.deleteAll();
    }

    @Test
    void returnsOrderWithLineItemsAndExactTotal() throws Exception {
        Customer customer = data.customer();
        Order order = data.order(customer, OffsetDateTime.now(),
                "Notebook:2:4.10", "USB-C Cable:1:0.20");

        mockMvc.perform(get("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getId()))
                .andExpect(jsonPath("$.lineItems.length()").value(2))
                .andExpect(jsonPath("$.total").value(8.40));
    }

    @Test
    void returns404ForUnknownOrder() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", 999_999))
                .andExpect(status().isNotFound());
    }
}
