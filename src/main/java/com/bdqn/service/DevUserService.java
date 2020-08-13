package com.bdqn.service;

import com.bdqn.pojo.DevUser;

public interface DevUserService {
    DevUser doLogin(String devCode, String devPassword);
}
