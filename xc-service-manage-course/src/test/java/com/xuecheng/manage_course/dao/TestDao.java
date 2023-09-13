package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseService courseService;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void testPageHelper(){
        //查询第一页，每页十条
        PageHelper.startPage(1,10);
        Page<CourseBase> courseList = courseMapper.findCourseList();
        List<CourseBase> result = courseList.getResult();
        //取页数
        long total = courseList.getTotal();
        System.out.println("总页数："+ total +"数据:\n"+ result);

    }


    //树形结构查询数据的处理
    @Test
    public void Tree(){
        List<CategoryNode> category = courseService.getCategory("1");
        //List<ArrayList<CategoryNode>> categoryy = courseService.getCategoryy("1");
        System.out.println(category);
    }
}
