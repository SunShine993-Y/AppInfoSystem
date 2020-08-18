package com.bdqn.dao.backend;

import com.bdqn.pojo.BackendUser;
import org.apache.ibatis.annotations.Param;

public interface BackendUserMapper {
    BackendUser doLogin(@Param("userCode") String userCode,@Param("userPassword") String userPassword);
}
