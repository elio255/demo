package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.models.Flight;
import com.example.demo.models.OrderEntity;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final FlightRepository flightRepo;

    public OrderService(OrderRepository orderRepo, FlightRepository flightRepo) {
        this.orderRepo = orderRepo;
        this.flightRepo = flightRepo;
    }

    public List<OrderDto> getAll() {
        return orderRepo.findAll().stream().map(this::toDto).toList();
    }

    public OrderDto getById(UUID id) {
        return orderRepo.findById(id).map(this::toDto).orElse(null);
    }

    public OrderDto create(OrderDto dto) {
        OrderEntity o = fromDto(dto, new OrderEntity());
        OrderEntity saved = orderRepo.save(o);
        return toDto(saved);
    }

    public OrderDto update(UUID id, OrderDto dto) {
        return orderRepo.findById(id).map(existing -> {
            OrderEntity updated = fromDto(dto, existing);
            OrderEntity saved = orderRepo.save(updated);
            return toDto(saved);
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!orderRepo.existsById(id)) return false;
        orderRepo.deleteById(id);
        return true;
    }

    private OrderDto toDto(OrderEntity o) {
        OrderDto dto = new OrderDto();
        dto.id = o.getId() != null ? o.getId().toString() : null;
        dto.orderDate = o.getOrderDate() != null ? o.getOrderDate().toString() : null;
        dto.deliveryDate = o.getDeliveryDate() != null ? o.getDeliveryDate().toString() : null;
        dto.item = o.getItem();
        dto.quantity = o.getQuantity();
        dto.comments = o.getComments();
        dto.orderById = o.getOrderById();
        dto.orderByName = o.getOrderByName();
        dto.flightIds = o.getFlights() == null ? List.of() : o.getFlights().stream().map(f -> f.getId().toString()).toList();
        return dto;
    }

    private OrderEntity fromDto(OrderDto dto, OrderEntity o) {
        if (dto == null) throw new IllegalArgumentException("Body is required");
        if (isBlank(dto.orderDate) || isBlank(dto.deliveryDate) || isBlank(dto.item)) {
            throw new IllegalArgumentException("Missing required order fields");
        }
        if (dto.quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        if (dto.orderById == null || isBlank(dto.orderByName)) {
            throw new IllegalArgumentException("OrderBy is required");
        }
        if (dto.flightIds == null || dto.flightIds.isEmpty()) {
            throw new IllegalArgumentException("At least one flight is required");
        }

        try {
            o.setOrderDate(LocalDate.parse(dto.orderDate));
            o.setDeliveryDate(LocalDate.parse(dto.deliveryDate));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format");
        }
        o.setItem(dto.item.trim());
        o.setQuantity(dto.quantity);
        o.setComments(isBlank(dto.comments) ? null : dto.comments.trim());
        o.setOrderById(dto.orderById);
        o.setOrderByName(dto.orderByName.trim());

        // Resolve flights from IDs
        List<Flight> flights = new ArrayList<>();
        for (String flightId : dto.flightIds) {
            if (isBlank(flightId)) continue;
            UUID id = UUID.fromString(flightId.trim());
            Flight f = flightRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightId));
            flights.add(f);
        }
        o.setFlights(flights);

        return o;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
