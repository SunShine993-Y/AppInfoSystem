package com.bdqn.service;

import com.bdqn.pojo.AppVersion;

import java.util.List;

public interface AppVersionService {
    List<AppVersion> appVersionAdd(Integer id);

    AppVersion findAppVersionByid(Integer vid);

    boolean VersionAdd(AppVersion appVersion);

    boolean updVersion(AppVersion appVersion);

    AppVersion findAppVersionById(Integer vid);
}
