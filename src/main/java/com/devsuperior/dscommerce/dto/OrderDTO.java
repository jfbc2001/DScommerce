package com.devsuperior.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderStatus;

public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;

    private UserDTO client;

    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Long id, Instant moment, OrderStatus status, UserDTO client) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        status = entity.getStatus();
        client = new UserDTO(entity.getClient());

        entity.getItems().forEach(item -> items.add(new OrderItemDTO(item)));
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UserDTO getClient() {
        return client;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;

        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }

        return sum;
    }
}