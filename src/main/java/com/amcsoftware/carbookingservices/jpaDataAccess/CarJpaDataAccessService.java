package com.amcsoftware.carbookingservices.jpaDataAccess;

import com.amcsoftware.carbookingservices.dao.CarDao;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarJpaDataAccessService implements CarDao {

    private final CarRepository carRepository;

    public CarJpaDataAccessService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public boolean carExistByMake(String make) {
        return carRepository.existsByMake(make);
    }

    @Override
    public Long carCount() {
        return carRepository.count();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getAllCars(String make) {
        return carRepository.findCarsBy(make);
    }

    @Override
    public List<Car> getAllCars(String make, String model) {
        return carRepository.findCarsBy(make, model);
    }


}
