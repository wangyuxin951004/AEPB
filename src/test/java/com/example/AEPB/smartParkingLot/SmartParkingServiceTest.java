package com.example.AEPB.smartParkingLot;

import com.example.AEPB.smartParkingLot.exception.NoFreeParkingSpaceException;
import com.example.AEPB.smartParkingLot.exception.PickUpException;
import com.example.AEPB.smartParkingLot.model.ParkingLot;
import com.example.AEPB.smartParkingLot.model.ParkingTicket;
import org.junit.Before;
import org.junit.Test;
import com.example.AEPB.smartParkingLot.model.Car;
import com.example.AEPB.smartParkingLot.service.SmartParkingService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SmartParkingServiceTest {

    private  SmartParkingService smartParkingService;

    @Before
    public void setUp() {
        smartParkingService = new SmartParkingService(
                List.of(ParkingLot.initParkingLot(8),ParkingLot.initParkingLot(9),ParkingLot.initParkingLot(10))
        );
    }

    @Test
    public void should_parking_success_when_parking_lot_has_space(){
        //given
        Car car = Car.builder().numberPlate("陕A001").build();

        //when
        ParkingTicket ticket = smartParkingService.parking(car);

        //then
        assertEquals("03-001-陕A001",ticket.toString());
    }

    @Test(expected = NoFreeParkingSpaceException.class)
    public void should_throw_exception_when_parking_lot_no_space(){
        //given
        smartParkingService = new SmartParkingService(
                List.of(ParkingLot.initParkingLot(0),ParkingLot.initParkingLot(0),ParkingLot.initParkingLot(0))
        );
        Car car = Car.builder().numberPlate("陕A001").build();

        //when
        smartParkingService.parking(car);
    }


    @Test
    public void should_pick_up_car_success_when_parking_lot_has_target_car(){
        //given
        ParkingTicket ticket = smartParkingService.parking(Car.builder().numberPlate("陕A001").build());

        //when
        Car car = smartParkingService.pickUp(ticket);

        //then
        assertEquals("陕A001",car.getNumberPlate());
    }

    @Test(expected = PickUpException.class)
    public void should_throw_exception_when_pick_up_failed() {
        smartParkingService.pickUp(ParkingTicket.builder().build());
    }
}
