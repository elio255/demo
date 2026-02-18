package com.example.demo.dto;

import java.util.List;

public class OrderDto {
    public String id;
    public String orderDate;      // ISO: YYYY-MM-DD
    public String deliveryDate;   // ISO: YYYY-MM-DD
    public String item;
    public int quantity;
    public String comments;       // nullable
    public List<String> flightIds;
    public Long orderById;
    public String orderByName;
}
