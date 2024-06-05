package com.fescnet.lab_dio_springboot_final.domain.repository;

import com.fescnet.lab_dio_springboot_final.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
