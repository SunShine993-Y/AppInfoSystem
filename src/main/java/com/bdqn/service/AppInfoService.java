package com.bdqn.service;

import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.DataDictionary;
import com.bdqn.pojo.QueryAppInfoVO;
import com.bdqn.util.PageBean;

import java.util.List;

public interface AppInfoService {
    PageBean<AppInfo> findAppList(QueryAppInfoVO queryAppInfoVO);

    List<DataDictionary> findDataDictionaryList(String param);
}
