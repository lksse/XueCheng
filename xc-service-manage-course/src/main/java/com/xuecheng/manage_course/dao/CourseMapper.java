package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseInfo;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {
   //根据ID查找课程基本信息
   CourseBase findCourseBaseById(String id);
   Page<CourseBase> findCourseList();
   //分页查找课程信息
   Page<CourseInfo> findCourseListInfo();
   CoursePic findCoursePic();
   //添加课程基本信息
   ResponseResult addCourseBase();
   List<CategoryNode> getCategory(String id);

}
