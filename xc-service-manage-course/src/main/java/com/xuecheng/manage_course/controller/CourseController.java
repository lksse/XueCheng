package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursepublishResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//记得转数据================================================@RestController
@RestController
//课程基本信息
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;
    //查找课程基本信息列表，分页
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {

        return courseService.getPageCourseBase(page, size);
    }
    //根据ID查找课程基本信息列表
    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseBase findCourseListById(@PathVariable("courseId") String courseId) {

        return courseService.getCourseBaseById(courseId);

    }


  /*  //TODO            查询课程图片
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCourseList(@PathVariable("page") int courseId) {

        return null;
    }*/
    /*//添加课程
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult AddCourse(@RequestBody CourseBase courseBase) {

    return null;
    }*/
    //查询课程分类
    @Override
    @GetMapping("/category/list")
    public List<CategoryNode> findList() {
        return courseService.getCategory("1");
    }

    //TODO 待修改
   /* @Override
    @GetMapping("/category/listt")
    public List<ArrayList<CategoryNode>> findListt() {
        return courseService.getCategoryy("1");
    }*/
   //添加课程基本信息
    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourse(courseBase);
    }
    //获取课程基础信息
    @Override
    @GetMapping("/coursebase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseService.getCourseBaseById(courseId);
    }
    //更新课程基础信息
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id,@RequestBody CourseBase courseBase) {
        return courseService.updateCourseBaseById(id,courseBase);
    }
    //获取课程营销信息
    @Override
    @GetMapping("/coursemarket/get/{id}")
    public CourseMarket getCourseMarketById(@PathVariable("id") String courseId) {
        return courseService.getCourseMarketbyId(courseId);
    }
    //更新课程营销信息
    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id,@RequestBody CourseMarket courseMarket) {
        CourseMarket courseMarket1 = courseService.updateCourseMarketById(id, courseMarket);
        if (courseMarket1 != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.NULL);
    }
    //添加课程图片
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId,@RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId,pic);
    }
    //查询课程图片
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }
    //课程视图查询
    @Override
            //courseview -----GET
    @GetMapping("/courseviewmod/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursepublishResult preView(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursepublishResult publish(@PathVariable("id") String id) {
        return courseService.publish(id);
    }
}
