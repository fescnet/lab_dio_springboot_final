package com.fescnet.lab_dio_springboot_final.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO used to create contracts
 */
@Getter @Setter
public class ContractRequestDto {

    protected Long id;
    protected Long proposalId;

    public ContractRequestDto() {}
}
