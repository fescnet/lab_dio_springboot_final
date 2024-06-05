package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;

public interface ProposalService {
    public Proposal findById(Long id);
    Proposal create(Proposal entity);
}
