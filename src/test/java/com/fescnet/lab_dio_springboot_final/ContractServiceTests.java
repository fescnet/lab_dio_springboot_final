package com.fescnet.lab_dio_springboot_final;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.ContractRepository;
import com.fescnet.lab_dio_springboot_final.domain.repository.ProposalRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.impl.ContractServiceImpl;
import com.fescnet.lab_dio_springboot_final.util.PojoProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
class ContractServiceTests {

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    PropertyService propertyService;

    @Autowired
    UserService userService;

    @Autowired
    ProposalService proposalService;

    @Autowired
    ProposalRepository proposalRepository;

    ContractService contractService;

    User owner;
    User tenant;
    Property property;
    Proposal proposal;

    @BeforeEach
    void setupDependencies() {
        contractService = new ContractServiceImpl(contractRepository, proposalRepository);
        owner = userService.create(PojoProvider.createValidUser());

        User user = PojoProvider.createValidUser();
        user.setEmail("tenant@email.com");
        tenant = userService.create(user);

        property = propertyService.create(PojoProvider.createValidProperty(owner));
        proposal = proposalRepository.save(PojoProvider.createValidProposal(tenant, property));
    }

    @Test
    @DisplayName("Create a valid contract")
    void validContractCanBeCreated() {
        Contract contractCreated = contractService.create(proposal.getId());
        Assertions.assertNotNull(contractCreated.getId(), "Contract was not created");
    }

    @Test
    @DisplayName("Fail to create a contract with an invalid proposal id")
    void contractWithInvalidProposalCannotBeCreated() {
        Assertions.assertThrows(RuntimeException.class, () -> contractService.create(4L), "Creating contract with invalid proposal should throw RuntimeException");
    }

}
