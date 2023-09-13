package com.xuecheng.api.sys;

import com.xuecheng.framework.domain.course.CourseGrade;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "cms的Sys接口", description = "系统接口Sys")
public interface SysControllerApi {
    @ApiOperation("根据type查询等级信息")
//    public QueryResponseResult getCourseGrade(String dType);
    public CourseGrade getCourseGrade(String dType);

}
