package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @GetMapping
    public List<UserDto> getAll() {
        return List.of(
                new UserDto(1L, "Elio"),
                new UserDto(2L, "Maya"),
                new UserDto(3L, "Karim"),
                new UserDto(4L, "Sara")
        );
    }
}
