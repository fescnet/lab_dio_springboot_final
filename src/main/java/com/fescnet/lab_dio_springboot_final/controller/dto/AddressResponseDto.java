package com.fescnet.lab_dio_springboot_final.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Output from ViaCep API.
 * It is in Brazilian Portuguese because this is a Brazilian API
 */
@Getter @Setter
public class AddressResponseDto {
    private String cep;          // Postal Code
    private String logradouro;   // Street
    private String complemento;  // Complement
    private String bairro;       // Neighborhood
    private String localidade;   // City
    private String uf;           // State (abbreviation)
    private String ibge;         // IBGE Code (Brazilian Institute of Geography and Statistics)
    private String gia;          // GIA Code (General Register of Addresses)
    private String ddd;          // Area Code
    private String siafi;        // SIAFI Code (Federal Administration Financial System)
}
