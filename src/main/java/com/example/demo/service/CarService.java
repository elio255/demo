package com.example.demo.service;

import com.example.demo.models.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository repo;

    public CarService(CarRepository repo) {
        this.repo = repo;
    }

    public List<Car> getAll() {
        return repo.findAll();
    }

    public Optional<Car> getById(Long id) {
        return repo.findById(id);
    }

    public Car createCar(Car car) {
        car.setId(null); // force INSERT
        return repo.save(car);
    }

    public Optional<Car> updateCar(Long id, Car updated) {
        return repo.findById(id).map(existing -> {
            existing.setBrand(updated.getBrand());
            existing.setModel(updated.getModel());
            existing.setYear(updated.getYear());
            return repo.save(existing);
        });
    }

    public boolean deleteCar(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
