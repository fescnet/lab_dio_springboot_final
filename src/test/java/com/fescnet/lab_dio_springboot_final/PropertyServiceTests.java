package com.fescnet.lab_dio_springboot_final;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.PropertyRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.impl.PropertyServiceImpl;
import com.fescnet.lab_dio_springboot_final.util.PojoProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
class PropertyServiceTests {

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	UserService userService;

	@Autowired
	ContractService contractService;

	@Autowired
	ProposalService proposalService;

	PropertyService propertyService;

	User user1;
	User user2;

	@BeforeEach
	void setupDependencies() {
		propertyService = new PropertyServiceImpl(propertyRepository, userService, contractService);
		user1 = userService.create(PojoProvider.createValidUser());

		User user = PojoProvider.createValidUser();
		user.setEmail("other@email.com");
		user2 = userService.create(user);
	}

	@Test
	@DisplayName("Create a valid property")
	void validPropertyCanBeCreated() {
		Property propertyToCreate = PojoProvider.createValidProperty(user1);
		Assertions.assertNull(propertyToCreate.getId(), "Property ID should be null before creation");
		Property propertyCreated = propertyService.create(propertyToCreate);
		Assertions.assertNotNull(propertyCreated.getId(), "Property ID should be assigned after creation");
	}

	@Test
	@DisplayName("Fail to create property with null city")
	void propertyWithNullCityCannotBeCreatedAndThrowsException() {
		Property invalidProperty = PojoProvider.createValidProperty(user1);
		invalidProperty.setCity(null);

		Assertions.assertThrows(RuntimeException.class, () -> propertyService.create(invalidProperty), "Creating property with null city should throw RuntimeException");
	}

	@Test
	@DisplayName("Fail to create property with null street")
	void propertyWithNullStreetCannotBeCreatedAndThrowsException() {
		Property invalidProperty = PojoProvider.createValidProperty(user1);
		invalidProperty.setStreet(null);

		Assertions.assertThrows(RuntimeException.class, () -> propertyService.create(invalidProperty), "Creating property with null street should throw RuntimeException");
	}

	@Test
	@DisplayName("Fail to create property with null number")
	void propertyWithNullNumberCannotBeCreatedAndThrowsException() {
		Property invalidProperty = PojoProvider.createValidProperty(user1);
		invalidProperty.setNumber(null);

		Assertions.assertThrows(RuntimeException.class, () -> propertyService.create(invalidProperty), "Creating property with null number should throw RuntimeException");
	}

	@Test
	@DisplayName("Fail to create property with null neighborhood")
	void propertyWithNullNeighborhoodCannotBeCreatedAndThrowsException() {
		Property invalidProperty = PojoProvider.createValidProperty(user1);
		invalidProperty.setNeighborhood(null);

		Assertions.assertThrows(RuntimeException.class, () -> propertyService.create(invalidProperty), "Creating property with null neighborhood should throw RuntimeException");
	}

	@Test
	@DisplayName("Delete a property with no contracts")
	void propertyWithNoContractsCanBeDeleted() {
		Property property = propertyService.create(PojoProvider.createValidProperty(user1));
		Assertions.assertNotNull(property.getId(), "Property ID should be assigned after creation");

		propertyService.delete(property.getId());
		Assertions.assertThrows(RuntimeException.class, () -> propertyService.findById(property.getId()), "Deleted property should not be found");
	}

	@Test
	@DisplayName("Fail to delete a property with active contracts")
	void propertyWithActiveContractsCannotBeDeleted() {
		Property property = propertyService.create(PojoProvider.createValidProperty(user1));
		Assertions.assertNotNull(property.getId(), "Property ID should be assigned after creation");

		Proposal proposal = proposalService.create(PojoProvider.createValidProposal(user2, property));
		Contract contract = contractService.create(PojoProvider.createValidContract(user2, property, user1, proposal));
		Assertions.assertNotNull(contract.getId(), "Contract ID should be assigned after creation");

		Assertions.assertThrows(RuntimeException.class, () -> propertyService.delete(property.getId()), "Deleting property with active contracts should throw RuntimeException");
	}

	@Test
	@DisplayName("Available properties list does not contain rented properties")
	void availablePropertiesListDoesNotContainRentedProperties() {
		Property rentedProperty = propertyService.create(PojoProvider.createValidProperty(user1));
		Proposal proposal = proposalService.create(PojoProvider.createValidProposal(user2, rentedProperty));
		Contract contract = contractService.create(PojoProvider.createValidContract(user2, rentedProperty, user1, proposal));
		Assertions.assertNotNull(contract.getId(), "Contract ID should be assigned after creation");

		Property availableProperty = propertyService.create(PojoProvider.createValidProperty(user1));
		Assertions.assertNotNull(availableProperty.getId(), "Property ID should be assigned after creation");

		List<Property> properties = propertyRepository.findAllWithoutActiveContracts();
		Assertions.assertEquals(1, properties.size(), "Available properties list should contain only one property");
		Assertions.assertEquals(availableProperty.getId(), properties.get(0).getId(), "Available property ID should match the expected ID");
	}

	@Test
	@DisplayName("Fail to change ownership of rented property")
	void rentedPropertiesCannotHaveTheirOwnershipChanged() {
		Property rentedProperty = propertyService.create(PojoProvider.createValidProperty(user1));
		Assertions.assertNotNull(rentedProperty.getId(), "Property ID should be assigned after creation");

		Proposal proposal = proposalService.create(PojoProvider.createValidProposal(user2, rentedProperty));
		Contract contract = contractService.create(PojoProvider.createValidContract(user2, rentedProperty, user1, proposal));
		Assertions.assertNotNull(contract.getId(), "Contract ID should be assigned after creation");

		rentedProperty.setOwner(user2);
		Assertions.assertThrows(RuntimeException.class, () -> propertyService.update(rentedProperty.getId(), rentedProperty), "Changing ownership of rented property should throw RuntimeException");
	}
}
