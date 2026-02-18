package com.example.demo.service;

import com.example.demo.dto.FlightDto;
import com.example.demo.models.Flight;
import com.example.demo.models.FlightDayEnum;
import com.example.demo.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    public List<FlightDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public FlightDto getById(UUID id) {
        return repo.findById(id).map(this::toDto).orElse(null);
    }

    public FlightDto create(FlightDto dto) {
        Flight f = fromDto(dto, new Flight());
        Flight saved = repo.save(f);
        return toDto(saved);
    }

    public FlightDto update(UUID id, FlightDto dto) {
        return repo.findById(id).map(existing -> {
            Flight updated = fromDto(dto, existing);
            Flight saved = repo.save(updated);
            return toDto(saved);
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    private FlightDto toDto(Flight f) {
        FlightDto dto = new FlightDto();
        dto.id = f.getId() != null ? f.getId().toString() : null;
        dto.flightDate = f.getFlightDate() != null ? f.getFlightDate().toString() : null;
        dto.flightTime = f.getFlightTime() != null ? f.getFlightTime().toString() : null;
        dto.isRecurrent = f.isRecurrent();
        dto.days = f.getDays() != null ? f.getDays().stream().map(Enum::name).collect(Collectors.toList()) : List.of();
        dto.effectiveStart = f.getEffectiveStart() != null ? f.getEffectiveStart().toString() : null;
        dto.effectiveEnd = f.getEffectiveEnd() != null ? f.getEffectiveEnd().toString() : null;
        dto.flightNo = f.getFlightNo();
        dto.startLocation = f.getStartLocation();
        dto.destination = f.getDestination();
        dto.aircraft = f.getAircraft();
        dto.capacity = f.getCapacity();
        return dto;
    }

    private Flight fromDto(FlightDto dto, Flight f) throws IllegalArgumentException {
        // Basic required validation
        if (dto == null) throw new IllegalArgumentException("Body is required");
        if (isBlank(dto.flightDate) || isBlank(dto.flightTime) || isBlank(dto.flightNo)
                || isBlank(dto.startLocation) || isBlank(dto.destination) || isBlank(dto.aircraft)) {
            throw new IllegalArgumentException("Missing required flight fields");
        }
        if (dto.capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");

        try {
            f.setFlightDate(LocalDate.parse(dto.flightDate));
            // Accept HH:mm or HH:mm:ss
            f.setFlightTime(LocalTime.parse(dto.flightTime));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date/time format");
        }

        f.setRecurrent(dto.isRecurrent);

        List<FlightDayEnum> days = (dto.days == null ? List.<String>of() : dto.days).stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .map(FlightDayEnum::valueOf)
                .collect(Collectors.toList());

        if (dto.isRecurrent && days.isEmpty()) {
            throw new IllegalArgumentException("Recurrent flights must include days");
        }
        f.setDays(days);

        if (!isBlank(dto.effectiveStart)) f.setEffectiveStart(LocalDate.parse(dto.effectiveStart));
        else f.setEffectiveStart(null);

        if (!isBlank(dto.effectiveEnd)) f.setEffectiveEnd(LocalDate.parse(dto.effectiveEnd));
        else f.setEffectiveEnd(null);

        if (f.getEffectiveStart() != null && f.getEffectiveEnd() != null
                && f.getEffectiveStart().isAfter(f.getEffectiveEnd())) {
            throw new IllegalArgumentException("effectiveStart must be <= effectiveEnd");
        }

        f.setFlightNo(dto.flightNo.trim());
        f.setStartLocation(dto.startLocation.trim());
        f.setDestination(dto.destination.trim());
        f.setAircraft(dto.aircraft.trim());
        f.setCapacity(dto.capacity);

        return f;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
