package com.bdqn.dao;

import com.bdqn.pojo.AppCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCategoryMapper {
    List<AppCategory> getAppCategoryListByParentId(@Param("pid") Integer pid);
}
