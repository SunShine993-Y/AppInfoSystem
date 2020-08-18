package com.bdqn.service.backend;

import com.bdqn.dao.backend.BackendUserMapper;
import com.bdqn.pojo.BackendUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BackendUserServiceImpl implements BackendUserService {

    @Resource
    private BackendUserMapper backendUserMapper;

    @Override
    public BackendUser doLogin(String userCode, String userPassword) {
        return backendUserMapper.doLogin(userCode,userPassword);
    }
}
