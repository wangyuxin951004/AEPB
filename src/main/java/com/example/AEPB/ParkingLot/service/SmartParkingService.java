package com.example.AEPB.ParkingLot.service;

import com.example.AEPB.ParkingLot.exception.NoFreeParkingSpaceException;
import com.example.AEPB.ParkingLot.exception.PickUpException;
import com.example.AEPB.ParkingLot.model.ParkingLot;
import com.example.AEPB.ParkingLot.model.ParkingTicket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.AEPB.ParkingLot.model.Car;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SmartParkingService {

    private List<ParkingLot> parkingLots;

    public ParkingTicket parking(Car car) {
        Optional<ParkingLot> parkingLot =
                parkingLots.stream().max(Comparator.comparing(ParkingLot::getFreeParkingSpaces));
        if (parkingLot.isPresent() && !parkingLot.get().getCarMap().containsValue(car)) {
            if(parkingLot.get().getFreeParkingSpaces() <= 0){
                throw new NoFreeParkingSpaceException("There is no free parking space");
            }
            List<Boolean> parkingSpaceStatus = parkingLot.get().getParkingSpaceStatus();
            for (int i = 0; i < parkingSpaceStatus.size(); i++) {
                if (Boolean.FALSE.equals(parkingSpaceStatus.get(i))) {
                    parkingLot.get().park(i, car);
                    return ParkingTicket.builder()
                            .parkingLotNumber(parkingLots.indexOf(parkingLot.get()))
                            .parkingSpaceNumber(i)
                            .numberPlate(car.getNumberPlate()).build();
                }
            }
        }
        return ParkingTicket.builder().build();
    }

    public Car pickUpCar(ParkingTicket parkingTicket) {
        try{
            return parkingLots.get(parkingTicket.getParkingLotNumber()).pick(parkingTicket.getParkingSpaceNumber());
        } catch (Exception e){
            throw new PickUpException(String.format("Try to pick up failed: %s", e));
        }
    }
}