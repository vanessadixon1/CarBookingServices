package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.jpaDataAccess.CarJpaDataAccessService;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService extends CarJpaDataAccessService {

    public CarService(CarRepository carRepository) {
        super(carRepository);
    }

    public List<Car> getCars() {
        if(carCount() == 0) {
            throw new ResourceNotFound("no cars found");
        }
       return getAllCars();
    }

    public List<Car> getCars(String make) {
        if(carCount() == 0 || !carExistByMake(make)) {
            throw new ResourceNotFound("The make [%s]".formatted(make) + " was not found");
        }
        return getAllCars(make);
    }

    public List<Car> getCars(String make, String model) {
        if(carCount() == 0 || (!carExistByMake(make) && !carExistByMake(model))) {
            throw new ResourceNotFound("The make [%s]".formatted(make) + " model [%s]".formatted(model) +  "  was not found");
        }
        return getAllCars(make,model);
    }


}
