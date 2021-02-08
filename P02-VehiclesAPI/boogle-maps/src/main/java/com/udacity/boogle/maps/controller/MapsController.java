package com.udacity.boogle.maps.controller;

import com.udacity.boogle.maps.model.Address;
import com.udacity.boogle.maps.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/maps")
public class MapsController {

    private AddressService addressService;

    public MapsController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public Address get(@RequestParam Long vehicleId, @RequestParam Double lat, @RequestParam Double lon) {
        System.out.println("get controller");
        return addressService.getAddress(vehicleId, lat, lon);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<?> delete(@PathVariable Long vehicleId) {
        System.out.println("delete controller");
        addressService.delete(vehicleId);
        return ResponseEntity.noContent().build();
    }

}
