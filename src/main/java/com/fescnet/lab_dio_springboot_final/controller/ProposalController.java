package com.fescnet.lab_dio_springboot_final.controller;

import com.fescnet.lab_dio_springboot_final.controller.dto.ProposalRequestDto;
import com.fescnet.lab_dio_springboot_final.controller.dto.ProposalResponseDto;
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
@RequestMapping("/proposals")
@Tag(name = "Proposal Controller", description = "RESTful API for creating proposals.")
public class ProposalController {

    private final ProposalService proposalService;
    private final UserService userService;

    public ProposalController(ProposalService proposalService, UserService userService) {
        this.proposalService = proposalService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new proposal", description = "Create a new proposal and return the created proposal's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proposal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid proposal data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Invalid operation")
    })
    public ResponseEntity<ProposalResponseDto> create(@RequestBody ProposalRequestDto proposalRequestDto) {
        // Business rule: The tenant of the proposal must be the authenticated user
        userService.throwIfNotTheSameAsJwtUser(proposalRequestDto.getTenantId());
        var proposal = proposalService.create(proposalRequestDto.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProposalResponseDto(proposal));
    }
}
