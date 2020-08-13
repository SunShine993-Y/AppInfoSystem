package com.bdqn.controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.pojo.AppCategory;
import com.bdqn.pojo.AppInfo;
import com.bdqn.pojo.DataDictionary;
import com.bdqn.pojo.QueryAppInfoVO;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.util.PageBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppInfoController {

    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppCategoryService appCategoryService;

    /**
     * 获取等级分类
     * ajax请求必须添加注解 @ResponseBody categorylevellist.json/1
     * @return
     */
    @RequestMapping("/categorylevellist.json/{pid}")
    @ResponseBody
    public String getCategoryList(@PathVariable Integer pid){
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
