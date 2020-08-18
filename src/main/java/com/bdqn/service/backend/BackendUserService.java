package com.bdqn.service.backend;

import com.bdqn.pojo.BackendUser;

public interface BackendUserService {
    BackendUser doLogin(String userCode, String userPassword);
}
