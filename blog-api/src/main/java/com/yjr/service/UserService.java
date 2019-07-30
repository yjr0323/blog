package com.yjr.service;

import com.yjr.entity.User;

import java.util.List;

/**
 * @author shimh
 * <p>
 * 2018年1月23日
 */
public interface UserService {

    List<User> findAll();

    User getUserByAccount(String account);

    User getUserById(Long id);

    Long saveUser(User user);

    Long updateUser(User user);

    void deleteUserById(Long id);
}
