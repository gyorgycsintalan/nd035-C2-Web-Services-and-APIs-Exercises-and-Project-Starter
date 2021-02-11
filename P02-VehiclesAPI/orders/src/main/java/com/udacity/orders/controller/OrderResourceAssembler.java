package com.udacity.orders.controller;

import com.udacity.orders.model.order.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>>  {

    @Override
    public Resource<Order> toResource(Order order) {
        return new Resource<>(order,
                linkTo(methodOn(OrderController.class).get(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).listOrders()).withRel("orders"));
    }
}
