package com.fescnet.lab_dio_springboot_final.controller;

import com.fescnet.lab_dio_springboot_final.controller.dto.ContractRequestDto;
import com.fescnet.lab_dio_springboot_final.controller.dto.ContractResponseDto;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/contracts")
@Tag(name = "Contracts Controller", description = "RESTful API for creating contracts.")
public class ContractController {

    private final ContractService contractService;
    private final UserService userService;
    private final ProposalService proposalService;

    public ContractController(ContractService contractService, UserService userService, ProposalService proposalService) {
        this.contractService = contractService;
        this.userService = userService;
        this.proposalService = proposalService;
    }

    @PostMapping
    @Operation(summary = "Create a new contract from a proposal", description = "Create a new contract from a proposal and return the created contract's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contract created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid contract data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Invalid operation")
    })
    public ResponseEntity<ContractResponseDto> create(@RequestBody ContractRequestDto contractRequestDto) {

        Proposal proposal = proposalService.findById(contractRequestDto.getProposalId());
        Property property = proposal.getProperty();

        // Business rule: only property owners can create contracts from proposals they received for their properties
        userService.throwIfNotTheSameAsJwtUser(property.getOwner().getId());

        var contractCreated = contractService.create(proposal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ContractResponseDto(contractCreated));
    }
}
