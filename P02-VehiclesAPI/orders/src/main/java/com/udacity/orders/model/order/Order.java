package com.udacity.orders.model.order;

import com.udacity.orders.model.Customer;
import com.udacity.orders.model.OrderStatus;
import com.udacity.orders.model.car.Car;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue
    private Long orderId;

    @Embedded
    @Valid
    private Car car;

    @Embedded
    @Valid
    private Customer buyer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(Long orderId, Car car, Customer buyer, @NotNull OrderStatus orderStatus) {
        this.orderId = orderId;
        this.car = car;
        this.buyer = buyer;
        this.orderStatus = orderStatus;
    }

    public Order(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
