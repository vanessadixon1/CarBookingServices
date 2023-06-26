package com.amcsoftware.carbookingservices.service.jpaDataAccess;

import com.amcsoftware.carbookingservices.dao.CarDao;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.model.Reservation;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import com.amcsoftware.carbookingservices.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("carJpa")
public class CarJpaDataAccessService implements CarDao {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public CarJpaDataAccessService(CarRepository carRepository, MemberRepository memberRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
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
        return carRepository.findCarsByMake(make);
    }

    @Override
    public List<Car> getAllCars(String make, String model) {
        return carRepository.findCarsByMakeAndModel(make, model);
    }

    @Override
    public Car findCarById(UUID id) {
        return carRepository.findCarByCarId(id);
    }

    @Override
    public boolean carExistById(UUID id) {
        return carRepository.existsByCarId(id);
    }

    @Override
    public void removeCar(Car car) {
        carRepository.delete(car);
    }

    @Override
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public boolean existsByLastNameAndEmail(String lastName, String email) {
        return memberRepository.existsByLastNameAndEmail(lastName, email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Long memberCount() {
        return memberRepository.count();
    }

    @Override
    public Member findMemberByLastNameAndEmail(String lastName, String email) {
        return memberRepository.findMemberByLastNameAndEmail(lastName, email);
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return memberRepository.findById(id);
    }

    @Override
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public boolean existsByReservationId(UUID id) {
        return reservationRepository.existsByReservationId(id);
    }

    @Override
    public Long reservationCount() {
        return reservationRepository.count();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public boolean existsById(UUID id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    @Override
    public Optional<Reservation> findReservationById(UUID id) {
        return reservationRepository.findById(id);
    }

    @Override
    public boolean reservationContainCar(Car car) {
        return reservationRepository.existsReservationByCar(car);
    }

    @Override
    public Reservation findReservationByCar(Car car) {
        return null;
    }

    @Override
    public Member findMemberById(UUID id) {
        return memberRepository.findByUserId(id);
    }



}
