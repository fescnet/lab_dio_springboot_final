package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.domain.model.User;

/**
 * check if the user been accessed is the same as in the JWT provided
 */
public interface UserService extends CrudService<Long, User> {
    public void throwIfNotTheSameAsJwtUser(User user);
    public void throwIfNotTheSameAsJwtUser(Long userId);
}
