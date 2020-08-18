package com.bdqn.dao;

import com.bdqn.pojo.AppVersion;

import java.util.List;

public interface AppVersionMapper {
    List<AppVersion> appVersionAdd(Integer id);

    AppVersion findAppVersionByid(Integer vid);

    int VersionAdd(AppVersion appVersion);

    int updVersion(AppVersion appVersion);
}
