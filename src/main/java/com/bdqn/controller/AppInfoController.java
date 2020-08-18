package com.bdqn.controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.pojo.*;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import com.bdqn.util.PageBean;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppInfoController {

    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppCategoryService appCategoryService;
    @Resource
    private AppVersionService appVersionService;

    @RequestMapping("/delapp.json")
    @ResponseBody
    public String delApk(Integer id){
        boolean result = appInfoService.delApk(id);
        String meg=null;
        if(!result){
            meg="failed";
        }else{
            meg="success";
        }
        return JSON.toJSONString(meg);
    }

    //下架
    @ResponseBody
    @RequestMapping("{appId}/sale.json")
    public String sale(@PathVariable Integer appId){
       //appInfoService.updStatus(appId);
        return "";
    }

    //appview/56
    @RequestMapping("appview/{id}")
    public String appview(Model model, @PathVariable Integer id){
        //app所有信息
        AppInfo appInfo = appInfoService.findAppInfoById(id);
        List<AppVersion> appVersionList = appVersionService.appVersionAdd(id);
        model.addAttribute("appVersionList",appVersionList);
        model.addAttribute("appInfo",appInfo);
        return "developer/appinfoview";
    }
    /**
     * 跳转添加app信息
     * @return
     */
    @RequestMapping("/appinfoadd")
    public String appInfoAdd(){
        return "developer/appinfoadd";
    }

    /**
     * 动态加载所属平台
     * @param tcode
     * @return
     */
    //datadictionarylist.json?tcode=APP_FLATFORM
    @RequestMapping("/datadictionarylist.json")
    @ResponseBody
    public String getTcodeList(@RequestParam String tcode){
        List<DataDictionary> flatFormList=appInfoService.findDataDictionaryList(tcode);
        return JSON.toJSONString(flatFormList);
    }

    /**appinfoaddsave添加app信息附带logo图片 需要进行文件上传
     * 1.form表单  enctype="multipart/form-data"
     *
     */
    @RequestMapping("appinfoaddsave")
    public String appInfoaddSave(HttpServletRequest request,@ModelAttribute AppInfo appInfo, @RequestParam("a_logoPicPath")MultipartFile multipartFile){
        /*图片地址*/
        String logoPicPath = null;
        String logoLocPath = null;
        //判断是否是文件上传
        if(!multipartFile.isEmpty()){
            //文件上传准备工作
            //1.指定上传的目录
            //获取相对路径的绝对路径
            String realPath = request.getSession().getServletContext().getRealPath("statics/uploadfiles");
            //2.定义上传文件的大小
            int fileSize=2097152;//2m
            //3.定义上传文件的类型
            List<String> fileNameList= Arrays.asList("jpg","png");
            //获取文件大小
            long size = multipartFile.getSize();
            //获取文件名
            String fileName = multipartFile.getOriginalFilename();
            //获取文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);
            //判断是否符合你文件的要求
            if(fileSize<size){//大小不合适
                request.setAttribute("fileUploadError","上传文件超过2M");
                return "developer/appinfoadd";
            }else if(!fileNameList.contains(extension)){//判断文件格式是否符合
                request.setAttribute("fileUploadError","文件格式不支持");
                return "developer/appinfoadd";
            }else{
                //重命名
                String newFileName = appInfo.getAPKName()+"."+extension;
                File dest = new File(realPath+File.separator+newFileName);
                try {
                    //进行文件上传
                    multipartFile.transferTo(dest);
                    //获取文件上传的地址，获取相对路径
                    logoPicPath = File.separator+"statics"+File.separator+"uploadfiles"+File.separator+newFileName;
                    //获取绝对路径
                    logoLocPath = realPath+File.separator+newFileName;
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
                //设置相对路径
                appInfo.setLogoPicPath(logoPicPath);
                //设置绝对路径
                appInfo.setLogoLocPath(logoLocPath);
                DevUser devUser =(DevUser) request.getSession().getAttribute("devUserSession");
                appInfo.setCreatedBy(devUser.getId());
                appInfo.setCreationDate(new Date());
                appInfo.setDevId(devUser.getId());

                boolean add = appInfoService.appInfoAdd(appInfo);
                if(!add){
                    return "developer/appinfoadd";
                }
            }
        }
        return  "redirect:/dev/flatform/app/list";
    }


    /**
     * 检查APKName是否存在
     * @param APKName
     * @return
     */
    @ResponseBody
    @RequestMapping("apkexist.json")
    public String checkAPKName(@RequestParam String APKName){
        Map<Object,String> map = new HashMap<Object, String>();
        if(APKName.isEmpty()){
            map.put("APKName","empty");
        }else if(appInfoService.apkNameExist(APKName)){
            map.put("APKName","exist");
        }else {
            map.put("APKName","noexist");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 获取等级分类
     * ajax请求必须添加注解 @ResponseBody categorylevellist.json/1
     * @return
     */
    @RequestMapping("/categorylevellist.json")
    @ResponseBody
    public String getCategoryList(Integer pid){
        List<AppCategory> appCategoryList  = appCategoryService.getAppCategoryListByParentId(pid);
        return JSON.toJSONString(appCategoryList);
    }

    /**
     * 想要通过对象来接收从后台传输过来的数据，使用注解@ModelAttribute
     */

    /**
     * 查询app列表
     * @param request
     * @param queryAppInfoVO
     * @return
     */
    @RequestMapping("/list")
    public String applist(HttpServletRequest request, @ModelAttribute QueryAppInfoVO queryAppInfoVO){
        //起始页
        if(queryAppInfoVO.getPageIndex() == null){
            queryAppInfoVO.setPageIndex(1);
        }
        //每页显示的条数
        queryAppInfoVO.setPageSize(5);
        PageBean<AppInfo> pages = appInfoService.findAppList(queryAppInfoVO);
        request.setAttribute("pages",pages);
        request.setAttribute("appInfoList",pages.getResult());

        //查询app状态
        List<DataDictionary> statusList=appInfoService.findDataDictionaryList("APP_STATUS");
        request.setAttribute("statusList",statusList);

        //查询所属平台
        List<DataDictionary> flatFormList=appInfoService.findDataDictionaryList("APP_FLATFORM");
        request.setAttribute("flatFormList",flatFormList);

        //查询一级分类信息
        List<AppCategory> categoryLevel1List  = appCategoryService.getAppCategoryListByParentId(null);
        request.setAttribute("categoryLevel1List",categoryLevel1List);

        //进行数据回显
        request.setAttribute("querySoftwareName",queryAppInfoVO.getQuerySoftwareName());
        request.setAttribute("queryStatus",queryAppInfoVO.getQueryStatus());
        request.setAttribute("queryFlatformId",queryAppInfoVO.getQueryFlatformId());
        request.setAttribute("queryCategoryLevel1",queryAppInfoVO.getQueryCategoryLevel1());
        request.setAttribute("queryCategoryLevel2",queryAppInfoVO.getQueryCategoryLevel2());
        request.setAttribute("queryCategoryLevel3",queryAppInfoVO.getQueryCategoryLevel3());

        //完善分类回显  如果穿了一级分类 代表触发过三级联动  认为你应该将二级分类中的信息全部查询到
        if(queryAppInfoVO.getQueryCategoryLevel1() != null){
            List<AppCategory> categoryLevel2List = appCategoryService.getAppCategoryListByParentId(queryAppInfoVO.getQueryCategoryLevel1());
            request.setAttribute("categoryLevel2List",categoryLevel2List);
        }
        if(queryAppInfoVO.getQueryCategoryLevel2() != null){
            List<AppCategory> categoryLevel3List = appCategoryService.getAppCategoryListByParentId(queryAppInfoVO.getQueryCategoryLevel2());
            request.setAttribute("categoryLevel3List",categoryLevel3List);
        }

        return "developer/appinfolist";
    }
}
