package com.amcsoftware.carbookingservices.controller;

import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.service.CarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/car")
public class carController {

    private final CarService carService;

    public carController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public List<Car> getCars() {
       return carService.getAllCars();
    }

    @GetMapping("/{make}")
    public List<Car> getCarsByMake(@PathVariable("make") String make) {
        return carService.getCars(make);
    }
}
