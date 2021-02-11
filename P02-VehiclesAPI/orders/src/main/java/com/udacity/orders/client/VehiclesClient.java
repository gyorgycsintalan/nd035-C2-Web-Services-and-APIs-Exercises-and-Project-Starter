package com.udacity.orders.client;

import com.udacity.orders.model.car.Car;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class VehiclesClient {

    private static final Logger log = LoggerFactory.getLogger(VehiclesClient.class);

    private final WebClient client;

    public VehiclesClient(WebClient vehiclesApi) {
        this.client = vehiclesApi;
    }

    /**
     * Gets a vehicle from the vehicles client, given vehicle ID.
     * @param vehicleId ID number of the vehicle to get
     * @return detailed vehicle information,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    public Car getCar(Long vehicleId) {
        Car car = null;
        try {
            car = client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cars/{vehicleId}")
                        .build(vehicleId)
                )
                .retrieve().bodyToMono(Car.class).block();

        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return car;
    }

}
