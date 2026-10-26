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
class OrderHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory data;

    @AfterEach
    void cleanUp() {
        data.deleteAll();
    }

    @Test
    void returnsCustomerOrdersNewestFirst() throws Exception {
        Customer customer = data.customer();
        OffsetDateTime now = OffsetDateTime.now();
        Order older = data.order(customer, now.minusDays(3), "Notebook:1:19.99");
        Order newer = data.order(customer, now.minusDays(1), "Desk Lamp:1:59.99", "USB-C Cable:2:12.35");

        mockMvc.perform(get("/api/customers/{id}/orders", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(newer.getId()))
                .andExpect(jsonPath("$[0].itemCount").value(3))
                .andExpect(jsonPath("$[1].orderId").value(older.getId()));
    }

    @Test
    void returnsEmptyListForCustomerWithNoOrders() throws Exception {
        Customer customer = data.customer();

        mockMvc.perform(get("/api/customers/{id}/orders", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
