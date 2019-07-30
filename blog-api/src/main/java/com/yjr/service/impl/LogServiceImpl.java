package com.yjr.service.impl;

import com.yjr.entity.Log;
import com.yjr.repository.LogRepository;
import com.yjr.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shimh
 * <p>
 * 2018年4月18日
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Integer saveLog(Log log) {
        return logRepository.save(log).getId();
    }
}
