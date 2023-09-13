package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.course.CourseGrade;
import com.xuecheng.manage_cms.dao.CmsSysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsSysService {
    @Autowired
    CmsSysRepository cmsSysRepository;
    /*public QueryResponseResult getCourseGrade(String d_type){
        CourseGrade byDType = cmsSysRepository.findByDType(d_type);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(byDType.getDValue());
        if (queryResult != null){
            return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        }
        return null;
    }*/

    //字典查询接口修改版本
    public CourseGrade getCourseGrade(String d_type){
        CourseGrade byDType = cmsSysRepository.findByDType(d_type);
        if (byDType != null){
            return byDType;
        }
        return null;
    }
}
