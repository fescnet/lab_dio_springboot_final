package com.fescnet.lab_dio_springboot_final.service.impl;

import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.UserRepository;
import com.fescnet.lab_dio_springboot_final.security.AuthenticatedUser;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.exception.BusinessException;
import com.fescnet.lab_dio_springboot_final.service.exception.InvalidDataException;
import com.fescnet.lab_dio_springboot_final.service.exception.NotFoundException;
import com.fescnet.lab_dio_springboot_final.service.exception.UserNotAllowedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public User create(User userToCreate) {
        throwIfNotValid(userToCreate);
        userToCreate.setId(null); // avoid updating a record using this method
        userToCreate.setPassword(encoder.encode(userToCreate.getPassword())); // encrypt the password to store on DB
        return this.userRepository.save(userToCreate);
    }

    private void throwIfNotValid(User user){
        ofNullable(user).orElseThrow(() -> new BusinessException("User must not be null."));
        int passLength = user.getPassword() != null ? user.getPassword().trim().length() : 0;
        if(passLength < 6 || passLength > 15){
            throw new InvalidDataException("Password can not start or end with spaces and it's length must be between 6 and 15.");
        }
    }

    @Transactional
    public User update(Long id, User userToUpdate) {
        throwIfNotValid(userToUpdate);
        User dbUser = this.findById(id);
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }
        dbUser.setName(userToUpdate.getName());
        dbUser.setEmail(userToUpdate.getEmail());
        dbUser.setPhone(userToUpdate.getPhone());
        dbUser.setPassword(encoder.encode(userToUpdate.getPassword())); // encrypt the password to store on DB
        return this.userRepository.save(dbUser);
    }

    public void throwIfNotTheSameAsJwtUser(User user){

        ofNullable(user).orElseThrow(() -> new BusinessException("User must not be null."));
        ofNullable(user.getEmail()).orElseThrow(() -> new BusinessException("E-mail must not be null."));

        if(!user.getId().equals(AuthenticatedUser.getId())){
            throw new UserNotAllowedException("The user is not allowed to perform this action");
        }
    }

    public void throwIfNotTheSameAsJwtUser(Long userId){

        ofNullable(userId).orElseThrow(() -> new BusinessException("userId must not be null."));
        if(userId == 0){ throw  new BusinessException("userId must not be zero."); }

        if(!userId.equals(AuthenticatedUser.getId())){
            throw new UserNotAllowedException("The user is not allowed to perform this action");
        }
    }

    @Transactional
    public void delete(Long id) {
        User dbUser = this.findById(id);
        this.userRepository.delete(dbUser);
    }
}

