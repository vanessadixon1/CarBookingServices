package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.dao.CarDao;
import com.amcsoftware.carbookingservices.exceptions.DuplicateResourceException;
import com.amcsoftware.carbookingservices.exceptions.ForbiddenResourceException;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFoundException;
import com.amcsoftware.carbookingservices.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarService  {

    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getCars() {
        if(carDao.carCount() == 0) {
            throw new ResourceNotFoundException("no cars found");
        }

        List<Car> availableCars = new ArrayList<>();

        for(Car car: carDao.getAllCars()) {
            if(!carDao.reservationContainCar(car)) {
                availableCars.add(car);
            }
        }
       return availableCars;
    }

    public List<Car> getCars(String make) {
        String locateMake = make.substring(0,1).toUpperCase() + make.substring(1);

        if(carDao.carCount() == 0) {
            throw new ResourceNotFoundException("no data available");
        }

        if(!carDao.carExistByMake(locateMake)) {
            throw new ResourceNotFoundException("The make %s".formatted(make) + " was not found");
        }

        List<Car> availableCars = new ArrayList<>();

        for(Car car : carDao.getAllCars(locateMake)) {
            if(!carDao.reservationContainCar(car)) {
                availableCars.add(car);
            }
        }

        return availableCars;
    }

    public List<Car> getCars(String make, String model) {

        String locateMake = make.substring(0,1).toUpperCase() + make.substring(1);

        String locateModel = model.substring(0,1).toUpperCase() + model.substring(1);

        if(carDao.carCount() == 0) {
            throw new ResourceNotFoundException("no data available");
        }

        if((!carDao.carExistByMake(locateMake) && !carDao.carExistByMake(locateModel))) {
            throw new ResourceNotFoundException("The make %s".formatted(make) + " and model %s".formatted(model) +  " was not found");
        }

        return carDao.getAllCars(locateMake,locateModel);
    }

    public void removeCarWithId(UUID id) {

        Car locatedCar = carDao.findCarById(id);

        if(!carDao.carExistById(id)) {
            throw new ResourceNotFoundException("the car %s".formatted(id) + " was not found");
        }

        if(carDao.reservationContainCar(locatedCar)) {
            throw new ForbiddenResourceException("the car %s".formatted(locatedCar) + " is currently booked ");
        }

        carDao.removeCar(locatedCar);
    }

    public void updateCar(UUID id, Car car) {
        if(!carDao.carExistById(id)) {
            throw new ResourceNotFoundException("id %s".formatted(id) + " was not found");
        }

        Car locatedCar = carDao.findCarById(id);

        if(!locatedCar.getMake().equals(car.getMake())) {
            locatedCar.setMake(car.getMake());
        }

        if(!locatedCar.getModel().equals(car.getModel())) {
            locatedCar.setModel(car.getModel());
        }

        if(locatedCar.getYear() != car.getYear()) {
            locatedCar.setYear(car.getYear());
        }

        if(!locatedCar.getPrice().equals(car.getPrice())) {
            locatedCar.setPrice(car.getPrice());
        }

        carDao.saveCar(locatedCar);

    }


}
