package com.bdqn.service;

import com.bdqn.dao.AppCategoryMapper;
import com.bdqn.pojo.AppCategory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AppCategoryServiceImpl implements AppCategoryService {

    @Resource
    private AppCategoryMapper appCategoryMapper;

    @Override
    public List<AppCategory> getAppCategoryListByParentId(Integer pid) {
        return appCategoryMapper.getAppCategoryListByParentId(pid);
    }
}
