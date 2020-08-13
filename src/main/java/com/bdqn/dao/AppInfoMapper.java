package com.bdqn.dao;

import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.DataDictionary;
import com.bdqn.pojo.QueryAppInfoVO;
import com.bdqn.util.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppInfoMapper {
    int getTotalCount(QueryAppInfoVO queryAppInfoVO);

    List<AppInfo> findAppInfo(QueryAppInfoVO queryAppInfoVO);

    List<DataDictionary> findDataDictionaryList(@Param("param") String param);
}
