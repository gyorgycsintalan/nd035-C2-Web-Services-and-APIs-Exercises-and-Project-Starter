package com.udacity.orders.controller;

import com.udacity.orders.model.OrderStatus;
import com.udacity.orders.model.order.Order;
import com.udacity.orders.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderResourceAssembler orderResourceAssembler;
    private OrderService orderService;

    public OrderController(OrderResourceAssembler orderResourceAssembler, OrderService orderService) {
        this.orderResourceAssembler = orderResourceAssembler;
        this.orderService = orderService;
    }

    /**
     * List information orders stored in repository together with related data requested from other services like Vehicles-api application
     * @return list of orders
     */
    @GetMapping
    Resources<Resource<Order>> listOrders() {
        List<org.springframework.hateoas.Resource<Order>> resources = orderService.listOrders().stream().map(orderResourceAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(OrderController.class).listOrders()).withSelfRel());
    }

    /**
     * Get order of given ID
     * @param orderId unique order identifier
     * @return requested order information
     */
    @GetMapping("/{orderId}")
    Resource<Order> get(@PathVariable Long orderId) {
        return orderResourceAssembler.toResource(orderService.get(orderId));
    }

    /**
     * Save a new order.
     * @param order Order object to save.
     * @return Response that the order was added to the system.
     * @throws URISyntaxException If the request contains invalid fields or syntax.
     */
    @PostMapping()
    ResponseEntity<?> save(@Valid @RequestBody Order order) throws URISyntaxException {
        orderService.saveOrder(order);
        Resource<Order> resource = orderResourceAssembler.toResource(order);

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }


    /**
     * Update stored order.
     * @param orderId Identifier of order to be updated.
     * @param order New order data to be saved.
     * @return Response that the order was added to the system.
     * @throws URISyntaxException If the request contains invalid fields or syntax.
     */
    @PutMapping("/{orderId}")
    ResponseEntity<?> update(@PathVariable Long orderId, @Valid @RequestBody Order order) throws URISyntaxException {
        order.setOrderId(orderId);
        orderService.saveOrder(order);
        Resource<Order> resource = orderResourceAssembler.toResource(order);

        return ResponseEntity.ok(resource);
    }

    /**
     * Update order status.
     * @param orderId Identifier of order to be updated.
     * @param status New status information to be saved.
     * @return Response that the order was added to the system.
     */
    @PatchMapping("/{orderId}")
    ResponseEntity<?> updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderService.get(orderId);
        order.setOrderStatus(status);
        orderService.saveOrder(order);
        Resource<Order> resource = orderResourceAssembler.toResource(order);

        return ResponseEntity.ok(resource);
    }

    /**
     * Delete stored order.
     * @param orderId Identifier of order to be deleted.
     * @return Empty response entity.
     */
    @DeleteMapping("/{orderId}")
    ResponseEntity<?> delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
