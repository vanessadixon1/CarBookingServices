package com.amcsoftware.carbookingservices.controller;

import com.amcsoftware.carbookingservices.customResponse.CustomResponse;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{make}/{model}")
    public List<Car> getCarsByMakeAndModel(@PathVariable("make") String make, @PathVariable("model") String model) {
        return carService.getCars(make, model);
    }

    @DeleteMapping("/removeCar/{id}")
    public ResponseEntity<CustomResponse> removeCarWithId(@PathVariable("id") UUID id) {
        carService.removeCarWithId(id);
        CustomResponse response = new CustomResponse();
        response.setMessage("Car with vin id: " + id  + " has been deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/updateCar/{id}")
    public ResponseEntity<CustomResponse> updateCar(@PathVariable("id") UUID id, @RequestBody Car car) {
        carService.updateCar(id, car);
        CustomResponse response = new CustomResponse();
        response.setMessage("Car with vin id: " + id  + " has been updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
