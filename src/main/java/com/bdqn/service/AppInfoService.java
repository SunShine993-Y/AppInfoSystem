package com.bdqn.service;

import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.DataDictionary;
import com.bdqn.pojo.QueryAppInfoVO;
import com.bdqn.util.PageBean;

import java.util.List;

public interface AppInfoService {
    PageBean<AppInfo> findAppList(QueryAppInfoVO queryAppInfoVO);

    List<DataDictionary> findDataDictionaryList(String param);

    boolean apkNameExist(String apkName);

    boolean appInfoAdd(AppInfo appInfo);

    AppInfo findAppInfoById(Integer id);

    boolean delApk(Integer id);

    boolean checkSave(Integer status, Integer id);

}
