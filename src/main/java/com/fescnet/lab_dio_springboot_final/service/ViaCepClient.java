package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.controller.dto.AddressResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    AddressResponseDto getAddressByCep(@PathVariable("cep") String cep);
}
