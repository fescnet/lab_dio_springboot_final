package com.fescnet.lab_dio_springboot_final.service.impl;

import com.fescnet.lab_dio_springboot_final.controller.dto.AddressResponseDto;
import com.fescnet.lab_dio_springboot_final.service.ViaCepClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final ViaCepClient viaCepClient;

    @Autowired
    public AddressService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public AddressResponseDto getAddressByCep(String cep) {
        return viaCepClient.getAddressByCep(cep);
    }
}
