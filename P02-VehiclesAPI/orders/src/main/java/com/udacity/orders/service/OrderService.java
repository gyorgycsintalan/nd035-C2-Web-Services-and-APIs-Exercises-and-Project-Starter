package com.udacity.orders.service;

import com.udacity.orders.client.VehiclesClient;
import com.udacity.orders.model.OrderStatus;
import com.udacity.orders.model.order.Order;
import com.udacity.orders.model.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private VehiclesClient vehiclesClient;
    private OrderRepository orderRepository;

    public OrderService(VehiclesClient vehiclesClient, OrderRepository orderRepository) {
        this.vehiclesClient = vehiclesClient;
        this.orderRepository = orderRepository;
    }

    /**
     * Gathers a list of all stored orders.
     * @return list of all orders in ordersRepository
     */
    public List<Order> listOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> {
            order.setCar(vehiclesClient.getCar(order.getCar().getId()));
        });

        return orders;
    }

    /**
     * Retrieves information about a stored order by unique identifier.
     * @param orderId unique ID of an order stored in the system
     * @return information about requested order including car data
     */
    public Order get(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.orElseThrow(OrderNotFoundException::new);
        order.setCar(vehiclesClient.getCar(order.getCar().getId()));

        return order;
    }

    /**
     * create or update order in repository
     * @param order order to be saved or updated
     * @return saved/updated order object
     */
    public Order saveOrder(Order order) {
        if (order.getOrderId() != null) {
            orderRepository.findById(order.getOrderId())
                    .map(orderToUpdate -> {
                        orderToUpdate.setCar(order.getCar());
                        orderToUpdate.setBuyer(order.getBuyer());
                        orderToUpdate.setOrderStatus(order.getOrderStatus());
                        return orderRepository.save(orderToUpdate);
                    }).orElseThrow(OrderNotFoundException::new);
        }

        return  orderRepository.save(order);
    }

    //update order status

    /**
     * update status of order specified by id
     * @param orderId unique ID of order
     * @param status new status value to be set
     */
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        if (orderId != null && status != null) {
            orderRepository.findById(orderId)
                    .map(orderToUpdate -> {
                        orderToUpdate.setOrderStatus(status);
                        return orderRepository.save(orderToUpdate);
                    }).orElseThrow(OrderNotFoundException::new);
        }
    }

    /**
     * delete order from repository by id
     * @param id unique ID of order
     */
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) orderRepository.deleteById(id);
    }

}
