package com.yjr.repository;


import com.yjr.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogRepository extends JpaRepository<Log, Integer> {
}
