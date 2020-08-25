package com.bdqn.service;

import com.bdqn.dao.AppVersionMapper;
import com.bdqn.pojo.AppVersion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionMapper appVersionMapper;

    @Override
    public List<AppVersion> appVersionAdd(Integer id) {
        return appVersionMapper.appVersionAdd(id);
    }

    @Override
    public AppVersion findAppVersionByid(Integer vid) {
        return appVersionMapper.findAppVersionByid(vid);
    }

    @Override
    public boolean VersionAdd(AppVersion appVersion) {
        if(appVersionMapper.VersionAdd(appVersion)>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updVersion(AppVersion appVersion) {
        if(appVersionMapper.updVersion(appVersion)>0){
            return  true;
        }
        return false;
    }

    @Override
    public AppVersion findAppVersionById(Integer vid) {
        return appVersionMapper.findAppVersionById(vid);
    }
}
