package com.bdqn.dao;

import com.bdqn.pojo.DevUser;
import org.apache.ibatis.annotations.Param;

public interface DevUserMaper {
    DevUser doLogin(@Param("devCode") String devCode, @Param("devPassword")String devPassword);
}
