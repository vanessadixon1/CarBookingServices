package com.amcsoftware.carbookingservices;

import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.model.Member;
//import com.amcsoftware.carbookingservices.repository.CarRepository;
//import com.amcsoftware.carbookingservices.repository.MemberRepository;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class CarBookingServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarBookingServicesApplication.class, args);

    }


//    @Bean
//    CommandLineRunner commandLineRunner(CarRepository carRepository) {
//        return orgs -> {
//            LocalDate date = LocalDate.of(2021, 03, 10);
//
//            Car honda = new Car("Honda", "CRV", 2010, new BigDecimal("99.99"));
//
//            Car honda2 = new Car("Honda", "CRV", 2010, new BigDecimal("99.99"));
//            Car bmw = new Car("BMW", "X5", 2023, new BigDecimal("210.50"));
//            Car rangRover = new Car("LandRover", "RangeRover", 2023, new BigDecimal("330.99"));
//            Car versa = new Car("Toyota", "Versa", 2000, new BigDecimal("29.99"));
//            Car camry = new Car("Toyota", "Camry", 2002, new BigDecimal("35.99"));
//
//            List<Car> cars = new ArrayList<>(List.of(honda, honda2, bmw, rangRover, versa));
//
//            carRepository.saveAll(cars);
//
//        };
//    }

}
