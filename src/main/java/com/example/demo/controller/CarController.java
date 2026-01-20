package com.example.demo.controller;

import com.example.demo.models.Car;
import com.example.demo.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // GET /api/cars
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAll();
    }

    // GET /api/cars/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/cars
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        if (car.getBrand() == null || car.getModel() == null || car.getYear() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Car created = carService.createCar(car);
        return ResponseEntity.ok(created);
    }

    // PUT /api/cars/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/cars/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        boolean deleted = carService.deleteCar(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
