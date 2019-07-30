package com.yjr.service;

import com.yjr.entity.User;

import java.util.List;


public interface UserService {

    List<User> findAll();

    User getUserByAccount(String account);

    User getUserById(Long id);

    Long saveUser(User user);

    Long updateUser(User user);

    void deleteUserById(Long id);
}
