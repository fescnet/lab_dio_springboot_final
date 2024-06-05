package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.domain.model.User;

import java.util.List;

public interface CrudService<ID, T> {
    List<T> findAll();
    T findById(ID id);
    User create(T entity);
    T update(ID id, T entity);
    void delete(ID id);
}
