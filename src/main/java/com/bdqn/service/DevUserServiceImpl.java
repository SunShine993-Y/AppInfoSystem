package com.bdqn.service;

import com.bdqn.dao.DevUserMaper;
import com.bdqn.pojo.DevUser;
import com.bdqn.service.DevUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class DevUserServiceImpl implements DevUserService {

    @Resource
    private DevUserMaper devUserMapper;

    @Override
    public DevUser doLogin(String devCode, String devPassword) {

        return devUserMapper.doLogin(devCode,devPassword);
    }
}
