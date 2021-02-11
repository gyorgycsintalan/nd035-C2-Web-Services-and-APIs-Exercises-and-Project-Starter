package com.udacity.orders;

import com.udacity.orders.client.VehiclesClient;
import com.udacity.orders.model.Customer;
import com.udacity.orders.model.OrderStatus;
import com.udacity.orders.model.car.Car;
import com.udacity.orders.model.order.Order;
import com.udacity.orders.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Order> json;

    @MockBean
    private OrderService orderService;

    @MockBean
    private VehiclesClient priceClient;

    /**
     * Creates pre-requisites for testing, such as an example Order.
     */
    @Before
    public void setup() {
        Order order = getOrder();
        order.setOrderId(2L);
        given(orderService.saveOrder(any())).willReturn(order);
        given(orderService.get(any())).willReturn(order);
        given(orderService.listOrders()).willReturn(Collections.singletonList(order));
    }

    /**
     * Tests for successful creation of new order in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Order order = getOrder();
        order.setOrderId(2L);
        mvc.perform(
                post(new URI("/orders"))
                        .content(json.write(order).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of orders.
     * @throws Exception if the read operation of the order list fails
     */
    @Test
    public void listCars() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   the whole list of vehicles. This should utilize the car from `getCar()`
         *   below (the vehicle will be the first in the list).
         */
        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$._embedded.orderList[0].orderId", is(2)));

        verify(orderService, times(1)).listOrders();
    }

    /**
     * Tests the read operation for a single order by ID.
     * @throws Exception if the read operation for a single order fails
     */
    @Test
    public void findOrder() throws Exception {

        mvc.perform(get("/orders/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.buyer.firstName", is("Bob")))
                .andExpect(jsonPath("$.buyer.lastName", is("Chalk")))
                .andExpect(jsonPath("$.buyer.address", is("Some street 789")))
                .andExpect(jsonPath("$.buyer.city", is("Wheretolive")))
                .andExpect(jsonPath("$.buyer.state", is("Proudstate")))
                .andExpect(jsonPath("$.buyer.zip", is("664455")))
                .andExpect(jsonPath("$.orderStatus", is("OPEN")));

        verify(orderService, times(1)).get(2L);

    }

    /**
     * Tests the deletion of an order car by ID.
     * @throws Exception if the delete operation of an order fails
     */
    @Test
    public void deleteOrder() throws Exception {
        mvc.perform(delete("/orders/2"))
                .andExpect(status().isNoContent());
        verify(orderService, times(1)).deleteOrder(2L);
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Order getOrder() {
        Order order = new Order();

        order.setOrderStatus(OrderStatus.OPEN);

        Car car = new Car();
        car.setId(2L);
        order.setCar(car);

        Customer customer = new Customer();
        customer.setAddress("Some street 789");
        customer.setCity("Wheretolive");
        customer.setState("Proudstate");
        customer.setZip("664455");
        customer.setFirstName("Bob");
        customer.setLastName("Chalk");
        order.setBuyer(customer);

        return order;
    }
}
