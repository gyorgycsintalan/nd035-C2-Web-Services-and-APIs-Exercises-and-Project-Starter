package com.udacity.boogle.maps.service;

import com.udacity.boogle.maps.model.Address;

import com.udacity.boogle.maps.model.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getAddress(Long vehicleId, Double lat, Double lon) {
        Address address;
        Optional<Address> optionalAddress = addressRepository.findById(vehicleId);

        if (optionalAddress.isPresent()) {
            address = optionalAddress.get();
        } else {
            address = new Address();
            address.setVehicleId(vehicleId);
        }

        if (optionalAddress.isPresent() &&
            address.getLat().equals(lat) &&
            address.getLon().equals(lon))
            return address;

        address.setLat(lat);
        address.setLon(lon);
        MockAddressRepository.setRandomValues(address);
        addressRepository.save(address);

        return address;
    }

    public void delete(Long vehicleId) {
        System.out.println("maps delete: " + vehicleId);
        Optional<Address> optionalAddress = addressRepository.findById(vehicleId);
        if (optionalAddress.isPresent())  addressRepository.delete(optionalAddress.get());
    }
}
