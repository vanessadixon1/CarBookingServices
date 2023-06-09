package com.amcsoftware.carbookingservices.dao;

import com.amcsoftware.carbookingservices.model.Car;

import java.util.List;

public interface CarDao {
    boolean carExistByMake(String make);
    Long carCount();
    List<Car> getAllCars();
    List<Car> getAllCars(String make);
    List<Car> getAllCars(String make, String model);
}
