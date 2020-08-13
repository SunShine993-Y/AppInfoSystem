package com.bdqn.service;

import com.bdqn.dao.AppInfoMapper;
import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.DataDictionary;
import com.bdqn.pojo.QueryAppInfoVO;
import com.bdqn.util.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppInfoServiceImpl implements AppInfoService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Override
    public PageBean<AppInfo> findAppList(QueryAppInfoVO queryAppInfoVO) {
        //1.创建pagebean对象
        PageBean<AppInfo> pageBean = new PageBean<>();
        pageBean.setCurrentPageNo(queryAppInfoVO.getPageIndex());
        pageBean.setPageSize(queryAppInfoVO.getPageSize());
        queryAppInfoVO.setStartIndex(pageBean.getStartIndex());
        //2.查询总记录数
        int count = appInfoMapper.getTotalCount(queryAppInfoVO);
        pageBean.setTotalCount(count);
        //3.查询结果集
        List<AppInfo> appInfo = appInfoMapper.findAppInfo(queryAppInfoVO);
        pageBean.setResult(appInfo);
        return pageBean;
    }

    @Override
    public List<DataDictionary> findDataDictionaryList(String param) {
        return appInfoMapper.findDataDictionaryList(param);
    }
}