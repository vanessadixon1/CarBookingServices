package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceExist;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.jpaDataAccess.CarJpaDataAccessService;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import com.amcsoftware.carbookingservices.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarService extends CarJpaDataAccessService {

    public CarService(CarRepository carRepository, MemberRepository memberRepository, ReservationRepository reservationRepository) {
        super(carRepository, memberRepository, reservationRepository);
    }

    public List<Car> getCars() {
        if(carCount() == 0) {
            throw new ResourceNotFound("no cars found");
        }
       return getAllCars();
    }

    public List<Car> getCars(String make) {
        String locateMake = make.substring(0,1).toUpperCase() + make.substring(1);
        if(carCount() == 0 || !carExistByMake(locateMake)) {
            throw new ResourceNotFound("The make [%s]".formatted(make) + " was not found");
        }
        System.out.println();
        return getAllCars(locateMake);
    }

    public List<Car> getCars(String make, String model) {
        String locateMake = make.substring(0,1).toUpperCase() + make.substring(1);
        String locateModel = model.substring(0,1).toUpperCase() + model.substring(1);
        if(carCount() == 0 || (!carExistByMake(locateMake) && !carExistByMake(locateModel))) {
            throw new ResourceNotFound("The make [%s]".formatted(make) + " model [%s]".formatted(model) +  "  was not found");
        }
        return getAllCars(locateMake,locateModel);
    }

    public void removeCarWithId(UUID id) {

        Car locatedCar = findCarById(id);
        if(!carExistById(id)) {
            throw new ResourceNotFound("the car [%s]".formatted(id) + " was not found");
        }

        if(reservationContainCar(locatedCar)) {
            throw new ResourceExist("the car [%s]".formatted(locatedCar) + " is currently booked ");
        }

        removeCar(locatedCar);
    }

    public void updateCar(UUID id, Car car) {
        if(!carExistById(id)) {
            throw new ResourceNotFound("id [%s]".formatted(id) + " was not found");
        }

        Car locatedCar = findCarById(id);

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

        saveCar(locatedCar);

    }


}
