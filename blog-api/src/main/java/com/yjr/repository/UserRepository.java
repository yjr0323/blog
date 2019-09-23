package com.yjr.repository;


import com.yjr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByAccount(String account);

}
