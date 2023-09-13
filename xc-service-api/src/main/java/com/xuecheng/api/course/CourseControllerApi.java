package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursepublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "课程管理",description = "课程管理",tags = {"课程管理"})
public interface CourseControllerApi {
    @ApiOperation("查询课程列表")
    public QueryResponseResult findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest
    );
    @ApiOperation("根据ID查询课程详细信息")
    public CourseBase findCourseListById(
            String id
    );
    /*@ApiOperation("查询我的课程图片")
    public CoursePic findCourseList(int id);*/
    /*@ApiOperation("添加课程")
    public ResponseResult AddCourse(CourseBase courseBase);*/

    @ApiOperation("查询分类")
    public List<CategoryNode> findList();
   /* @ApiOperation("分类2")
    public List<ArrayList<CategoryNode>> findListt();*/

    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    public CourseBase getCourseBaseById(String courseId) throws RuntimeException;

    @ApiOperation("更新课程基础信息")
    public ResponseResult updateCourseBase(String id,CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId ,String pic);
    @ApiOperation("查询课程图片")
    public CoursePic findCoursePic(String courseId);
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("查询课程生成模板数据接口,课程视图查询")
    public CourseView courseView(String id);

    @ApiOperation("预览课程")
    public CoursepublishResult preView(String id);

    @ApiOperation("课程发布")
    public CoursepublishResult publish(String id);
}
