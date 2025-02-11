package com.example.AEPB.parkingLot.model;

import com.example.AEPB.parkingLot.exception.InitParkingLotException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {

    private Integer freeParkingSpaces;

    private List<Boolean> parkingSpaceStatus;

    private Map<Integer, Car> carMap;

    private Map<String, Integer> parkingLotStatistic;

    public static ParkingLot initParkingLot(Integer totalParkingSpaces) {
        if (totalParkingSpaces < 0) {
            throw new InitParkingLotException("Init parking lot failed.");
        }
        List<Boolean> parkingSpaceStatus = new ArrayList<>(Arrays.asList(new Boolean[totalParkingSpaces]));
        Collections.fill(parkingSpaceStatus, Boolean.FALSE);
        return ParkingLot.builder()
                .freeParkingSpaces(totalParkingSpaces)
                .parkingSpaceStatus(parkingSpaceStatus)
                .carMap(new HashMap<>(totalParkingSpaces))
                .parkingLotStatistic(new HashMap<>())
                .build();
    }

    public void park(Integer parkingSpace, Car car, String operator){
        freeParkingSpaces--;
        parkingSpaceStatus.set(parkingSpace, true);
        carMap.put(parkingSpace, car);
        parkingLotStatistic.put(operator, parkingLotStatistic.getOrDefault(operator, 0) + 1);
    }

    public Car pick(Integer parkingSpace){
        freeParkingSpaces++;
        parkingSpaceStatus.set(parkingSpace, false);
        Car pickCar = carMap.get(parkingSpace);
        carMap.remove(parkingSpace);
        return pickCar;
    }

    public Integer getFreeParkingSpaces() {
        return freeParkingSpaces < 0 ? 0 : freeParkingSpaces;
    }

    public Integer getParkingLotVacancyRate() {
        try {
            return freeParkingSpaces / parkingSpaceStatus.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer getParkingLotStatistic(String operator){
        return parkingLotStatistic.getOrDefault(operator, 0);
    }
}