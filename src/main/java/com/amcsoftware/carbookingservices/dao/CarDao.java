package com.amcsoftware.carbookingservices.dao;

import com.amcsoftware.carbookingservices.model.Car;

import java.util.List;
import java.util.UUID;

public interface CarDao extends ReservationDao{
    boolean carExistByMake(String make);
    Long carCount();
    List<Car> getAllCars();
    List<Car> getAllCars(String make);
    List<Car> getAllCars(String make, String model);
    Car findCarById(UUID id);
    boolean carExistById(UUID id);
    void removeCar(Car car);
}
