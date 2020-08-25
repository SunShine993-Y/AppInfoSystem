package com.bdqn.controller.backend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdqn.pojo.*;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import com.bdqn.util.JedisUtils;
import com.bdqn.util.PageBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/manager/backend/app")
public class BackedAppinfoController {
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppCategoryService appCategoryService;
    @Resource
    private AppVersionService appVersionService;

    @RequestMapping("/checksave")
    public String checkSave(Integer status,Integer id){
        if (appInfoService.checkSave(status,id)){

            return "redirect:/manager/backend/app/list";
        }
        return "backend/appcheck";
    }

    @RequestMapping("/check")
    public String check(Model model,Integer aid, Integer vid){
        AppInfo appInfo = appInfoService.findAppInfoById(aid);
        AppVersion appVersion = appVersionService.findAppVersionById(vid);
        model.addAttribute("appInfo",appInfo);
        model.addAttribute("appVersion",appVersion);
        return "backend/appcheck";
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
        //后台审核条件 为1 待审核
        queryAppInfoVO.setQueryStatus(1);

        // 开始缓存   先判断redis中有没有这些数据
        // 第一次进来的时候 是从mysql数据库找那个查询数据  有点慢
        // 但是查到的数据保存到redis中  第二次查询的时候 判断redis中有没有这条数据
        // 如果有直接redis中获取数据 快
        PageBean<AppInfo> pages = appInfoService.findAppList(queryAppInfoVO);
        //查询app状态
        List<DataDictionary> statusList = appInfoService.findDataDictionaryList("APP_STATUS");
        //查询所属平台
        List<DataDictionary> flatFormList = appInfoService.findDataDictionaryList("APP_FLATFORM");
        //查询一级分类信息
        List<AppCategory> categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);


        request.setAttribute("pages",pages);
        request.setAttribute("appInfoList",pages.getResult());
        request.setAttribute("statusList",statusList);
        request.setAttribute("flatFormList",flatFormList);
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

        return "backend/applist";
    }
}
