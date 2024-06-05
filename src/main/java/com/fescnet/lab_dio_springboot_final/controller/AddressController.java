package com.fescnet.lab_dio_springboot_final.controller;

import com.fescnet.lab_dio_springboot_final.controller.dto.AddressResponseDto;
import com.fescnet.lab_dio_springboot_final.service.impl.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@Tag(name = "Address Controller", description = "RESTful API for providing address information to frontend applications.")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Retrieves address information for the provided zip code
     * Clients can use this endpoint on the frontend application to help users fill in the property's address information
     * @param cep expected format 99999-999
     * @return AddressResponse the Brazilian address related to the provided zip code
     */
    @GetMapping("/{cep}")
    public AddressResponseDto getAddressByCep(@PathVariable String cep) {
        return addressService.getAddressByCep(cep);
    }
}
