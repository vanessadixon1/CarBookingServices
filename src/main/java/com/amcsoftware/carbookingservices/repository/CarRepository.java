package com.amcsoftware.carbookingservices.repository;

import com.amcsoftware.carbookingservices.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    boolean existsByMake(String make);
    List<Car> findCarsByMake(String make);
    List<Car> findCarsByMakeAndModel(String make, String model);
    Car findCarByCarId(UUID id);
    boolean existsByCarId(UUID id);

}
