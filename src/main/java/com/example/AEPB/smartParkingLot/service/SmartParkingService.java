package com.example.AEPB.smartParkingLot.service;

import com.example.AEPB.smartParkingLot.model.ParkingLot;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SmartParkingService extends ParkingService{

    private final List<ParkingLot> parkingLots;

    public SmartParkingService(List<ParkingLot> parkingLots) {
        super(parkingLots);
        this.parkingLots = parkingLots;
    }

    @Override
    protected Optional<ParkingLot> getParkingLot() {
        return parkingLots.stream().max(Comparator.comparing(ParkingLot::getFreeParkingSpaces));
    }
}
