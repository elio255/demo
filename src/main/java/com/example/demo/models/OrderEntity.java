package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "item", nullable = false)
    private String item;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "order_by_id", nullable = false)
    private Long orderById;

    @Column(name = "order_by_name", nullable = false)
    private String orderByName;

    @ManyToMany
    @JoinTable(
            name = "order_flights",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private List<Flight> flights = new ArrayList<>();

    public OrderEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Long getOrderById() { return orderById; }
    public void setOrderById(Long orderById) { this.orderById = orderById; }

    public String getOrderByName() { return orderByName; }
    public void setOrderByName(String orderByName) { this.orderByName = orderByName; }

    public List<Flight> getFlights() { return flights; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }
}
