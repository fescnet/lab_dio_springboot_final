package com.fescnet.lab_dio_springboot_final.controller;

import com.fescnet.lab_dio_springboot_final.controller.dto.PropertyRequestDto;
import com.fescnet.lab_dio_springboot_final.controller.dto.PropertyResponseDto;
import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/properties")
@Tag(name = "Properties Controller", description = "RESTful API for managing properties.")
public class PropertyController {

    private final PropertyService propertyService;
    private final UserService userService;
    private final ContractService contractService;

    public PropertyController(PropertyService propertyService, UserService userService, ContractService contractService) {
        this.propertyService = propertyService;
        this.userService = userService;
        this.contractService = contractService;
    }

    @GetMapping("/available")
    @Operation(summary = "Get all properties", description = "Retrieve a list of all registered properties available for a new rental contract")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<PropertyResponseDto>> findAll() {
        //Business Rule: properties are public
        var properties = propertyService.findPropertiesWithoutActiveContracts();
        var propertyResponseDtos = properties.stream().map(PropertyResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(propertyResponseDtos);
    }

    @PostMapping
    @Operation(summary = "Create a new property", description = "Create a new property and return the created property's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Property created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid property data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PropertyResponseDto> create(@RequestBody PropertyRequestDto propertyRequestDto) {
        //Business rule: A user can register properties only for himself.
        userService.throwIfNotTheSameAsJwtUser(propertyRequestDto.getOwnerId());

        var property = propertyService.create(propertyRequestDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(property.getId())
                .toUri();
        return ResponseEntity.created(location).body(new PropertyResponseDto(property));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a property by ID", description = "Retrieve a specific property based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    public ResponseEntity<PropertyResponseDto> findById(@PathVariable Long id) {
        // Business rule: property details are public anytime
        var property = propertyService.findById(id);
        return ResponseEntity.ok(new PropertyResponseDto(property));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a property", description = "Update the data of a property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property updated successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found"),
            @ApiResponse(responseCode = "400", description = "Invalid property data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PropertyResponseDto> update(@PathVariable Long id, @RequestBody PropertyRequestDto propertyDto) {
        //Business rule: a user can update their own properties only.
        userService.throwIfNotTheSameAsJwtUser(propertyDto.getOwnerId());

        Property updatedProperty = propertyService.update(id, propertyDto.toModel());
        return ResponseEntity.ok(new PropertyResponseDto(updatedProperty));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a property", description = "Delete an existing property based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Property deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Property not found"),
            @ApiResponse(responseCode = "409", description = "Invalid operation")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Business rule: users can delete only their own properties
        Property property = propertyService.findById(id);
        userService.throwIfNotTheSameAsJwtUser(property.getOwner().getId());

        // Business Rule: the user can not delete a property if the property has an active contract
        Optional<Contract> contractOptional = contractService.findActiveContractByPropertyId(id);
        if(contractOptional.isPresent()){ throw new BusinessException("The property cannot be deleted because it has an active rental contract");        }

        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
