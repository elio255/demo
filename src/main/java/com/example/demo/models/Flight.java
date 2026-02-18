package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @Column(name = "flight_time", nullable = false)
    private LocalTime flightTime;

    @Column(name = "is_recurrent", nullable = false)
    private boolean isRecurrent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "flight_days", joinColumns = @JoinColumn(name = "flight_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private List<FlightDayEnum> days = new ArrayList<>();

    @Column(name = "effective_start")
    private LocalDate effectiveStart;

    @Column(name = "effective_end")
    private LocalDate effectiveEnd;

    @Column(name = "flight_no", nullable = false)
    private String flightNo;

    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "aircraft", nullable = false)
    private String aircraft;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    public Flight() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getFlightDate() { return flightDate; }
    public void setFlightDate(LocalDate flightDate) { this.flightDate = flightDate; }

    public LocalTime getFlightTime() { return flightTime; }
    public void setFlightTime(LocalTime flightTime) { this.flightTime = flightTime; }

    public boolean isRecurrent() { return isRecurrent; }
    public void setRecurrent(boolean recurrent) { isRecurrent = recurrent; }

    public List<FlightDayEnum> getDays() { return days; }
    public void setDays(List<FlightDayEnum> days) { this.days = days; }

    public LocalDate getEffectiveStart() { return effectiveStart; }
    public void setEffectiveStart(LocalDate effectiveStart) { this.effectiveStart = effectiveStart; }

    public LocalDate getEffectiveEnd() { return effectiveEnd; }
    public void setEffectiveEnd(LocalDate effectiveEnd) { this.effectiveEnd = effectiveEnd; }

    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getAircraft() { return aircraft; }
    public void setAircraft(String aircraft) { this.aircraft = aircraft; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
