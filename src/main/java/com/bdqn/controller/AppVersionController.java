package com.bdqn.controller;

import com.bdqn.pojo.AppCategory;
import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.AppVersion;
import com.bdqn.pojo.DevUser;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @RequestMapping("/appversionadd")
    public String appversionadd(Model model, Integer id){
        /*appVersionList*/
        List<AppVersion> appVersionList=appVersionService.appVersionAdd(id);
        model.addAttribute("appVersionList",appVersionList);
        AppVersion appVersion = new AppVersion();
        appVersion.setAppId(id);
        model.addAttribute("appVersion",appVersion);
        return "developer/appversionadd";
    }

    //appversionmodify?vid=41&aid=57
    @RequestMapping("/appversionmodify")
    public String appversionmodify(Model model,Integer vid,Integer aid){
        List<AppVersion> appVersionList=appVersionService.appVersionAdd(aid);

        model.addAttribute("appVersionList",appVersionList);

        AppVersion appVersion = appVersionService.findAppVersionByid(vid);
        model.addAttribute("appVersion",appVersion);

        return "developer/appversionmodify";
    }

    //appversionmodifysave 修改版本信息
    @RequestMapping("appversionmodifysave")
    public String appversionmodifysave(@ModelAttribute AppVersion appVersion){
        boolean upd = appVersionService.updVersion(appVersion);
        if(!upd){
            return "developer/appversionmodify";
        }
        return "redirect:/dev/flatform/app/list";
    }

    //addversionsave增加版本信息
    @RequestMapping("addversionsave")
    public String addVersionSave(HttpServletRequest request, @ModelAttribute AppVersion appVersion,Integer appId , @RequestParam("a_downloadLink")MultipartFile multipartFile){
        ///AppInfoSystem/statics/uploadfiles/com.doodleapps.powdertoy-V1.1.31.apk
        //下载apk地址
        String downloadLink=null;
        String apkLocPath = null;
        //判断是否是文件上传
        if(!multipartFile.isEmpty()){
            //1.指定上传的目录
            String realPath = request.getSession().getServletContext().getRealPath("statics/uploadfiles");
            //2.指定上传文件的类型
            List<String > fileNameList = Arrays.asList("apk");
            //3.获取文件名
            String fileName= multipartFile.getOriginalFilename();
            //4.获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            //判断文件格式是否支持
            if(!fileNameList.contains(extension)){
                request.setAttribute("fileUploadError","文件格式不支持");
                return "developer/appversionadd";
            }else{
                //重命名
                String newFileName = fileName;
                File dest = new File(realPath+File.separator+newFileName);
                try {
                    //进行文件上传
                    multipartFile.transferTo(dest);
                    //获取文件下载的地址 ，获取相对路径
                    downloadLink = File.separator+"statics"+File.separator+"uploadfiles"+File.separator+newFileName;
                    //获取绝对路径
                    apkLocPath = realPath + File.separator+newFileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //设置相对路径
                appVersion.setDownloadLink(downloadLink);
                //设置绝对路径
                appVersion.setApkLocPath(apkLocPath);
                DevUser devUser = (DevUser) request.getSession().getAttribute("devUserSession");
                appVersion.setCreatedBy(devUser.getId());
                appVersion.setCreationDate(new Date());
                appVersion.setAppId(appId);
                appVersion.setApkFileName(newFileName);
                boolean add = appVersionService.VersionAdd(appVersion);
                if(!add){
                    return "developer/appversionadd";
                }
            }
        }
        return "redirect:/dev/flatform/app/list";
    }

}
