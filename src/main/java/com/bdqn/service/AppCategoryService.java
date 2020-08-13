package com.bdqn.service;

import com.bdqn.pojo.AppCategory;

import java.util.List;

public interface AppCategoryService {
    List<AppCategory> getAppCategoryListByParentId(Integer pid);
}
