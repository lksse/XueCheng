package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.sys.SysControllerApi;
import com.xuecheng.framework.domain.course.CourseGrade;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsSysRepository;
import com.xuecheng.manage_cms.service.CmsSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/dictionary")
public class CmySysController implements SysControllerApi {
    @Autowired
    CmsSysService cmsSysService;


    @Override
    @GetMapping("/get/{dType}")
    public CourseGrade getCourseGrade(@PathVariable("dType") String d_type) {
        return cmsSysService.getCourseGrade(d_type);
    }
}
