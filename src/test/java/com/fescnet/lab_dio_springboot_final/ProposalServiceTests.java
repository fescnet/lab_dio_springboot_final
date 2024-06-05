package com.fescnet.lab_dio_springboot_final;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.ProposalRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.impl.ProposalServiceImpl;
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
class ProposalServiceTests {

    @Autowired
    ProposalRepository proposalRepository;

    @Autowired
    PropertyService propertyService;

    @Autowired
    UserService userService;

    @Autowired
    ContractService contractService;

    ProposalService proposalService;

    User owner;
    User tenant;
    Property property;

    @BeforeEach
    void setupDependencies() {
        proposalService = new ProposalServiceImpl(proposalRepository, propertyService, contractService);
        owner = userService.create(PojoProvider.createValidUser());

        User user = PojoProvider.createValidUser();
        user.setEmail("tenant@email.com");
        tenant = userService.create(user);

        property = propertyService.create(PojoProvider.createValidProperty(owner));
    }

    @Test
    @DisplayName("Create a valid proposal")
    void validProposalCanBeCreated() {
        Proposal proposalToCreate = PojoProvider.createValidProposal(tenant, property);
        Assertions.assertNull(proposalToCreate.getId(), "Proposal ID should be null before creation");
        Proposal proposalCreated = proposalService.create(proposalToCreate);
        Assertions.assertNotNull(proposalCreated.getId(), "Proposal ID should be assigned after creation");
    }

    @Test
    @DisplayName("Fail to create a proposal for a property with an active contract")
    void proposalForPropertyWithActiveContractCannotBeCreated() {
        Proposal proposal = proposalService.create(PojoProvider.createValidProposal(tenant, property));
        Contract contract = contractService.create(PojoProvider.createValidContract(tenant, property, owner, proposal));
        Assertions.assertNotNull(contract.getId(), "Contract ID should be assigned after creation");
        Proposal newProposal = PojoProvider.createValidProposal(tenant, property);
        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(newProposal), "Creating proposal for property with active contract should throw RuntimeException");
    }

    @Test
    @DisplayName("Fail to create a proposal by property owner")
    void ownerCannotCreateProposalForOwnProperty() {
        Proposal invalidProposal = PojoProvider.createValidProposal(owner, property);
        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal), "Property owner should not be able to create proposal for own property");
    }

    @Test
    @DisplayName("Fail to create a proposal with null start date")
    void proposalWithNullStartDateCannotBeCreatedAndThrowsException() {
        Proposal invalidProposal = PojoProvider.createValidProposal(tenant, property);
        invalidProposal.setStartDate(null);

        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal), "Creating proposal with null start date should throw RuntimeException");
    }

    @Test
    @DisplayName("Fail to create a proposal with null end date")
    void proposalWithNullEndDateCannotBeCreatedAndThrowsException() {
        Proposal invalidProposal = PojoProvider.createValidProposal(tenant, property);
        invalidProposal.setEndDate(null);

        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal), "Creating proposal with null end date should throw RuntimeException");
    }

    @Test
    @DisplayName("Fail to create a proposal with end date before start date")
    void proposalWithEndDateBeforeStartDateCannotBeCreatedAndThrowsException() {
        Proposal invalidProposal = PojoProvider.createValidProposal(tenant, property);
        invalidProposal.setEndDate(invalidProposal.getStartDate().minusDays(1));

        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal), "Creating proposal with end date before start date should throw RuntimeException");
    }

    @Test
    @DisplayName("Fail to create a proposal to rent a property for more than 30 months")
    void proposalToRentForMoreThan30MonthsThrowsException() {
        Proposal invalidProposal = PojoProvider.createValidProposal(tenant, property);
        invalidProposal.setEndDate(PojoProvider.moreThanThreeYearsFromToday());

        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal));
    }

    @Test
    @DisplayName("Fail to create a proposal to rent a property for less than a year")
    void proposalToRentForLessThanAYearThrowsException() {
        Proposal invalidProposal = PojoProvider.createValidProposal(tenant, property);
        invalidProposal.setEndDate(PojoProvider.twentyDaysFromToday());

        Assertions.assertThrows(RuntimeException.class, () -> proposalService.create(invalidProposal));
    }
}
