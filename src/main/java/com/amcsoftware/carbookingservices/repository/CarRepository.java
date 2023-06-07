package com.amcsoftware.carbookingservices.repository;

import com.amcsoftware.carbookingservices.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
}
