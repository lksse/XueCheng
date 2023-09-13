package com.xuecheng.manage_course;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.service.CourseService;
import jdk.nashorn.internal.runtime.regexp.joni.constants.RegexState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRibbon() {
        //确定服务名
        String ServiceID = "XC-SERVICE-MANAGE-CMS";
        //ribbon客户端从eurake获取服务列表,根据服务名获取服务列表
        //ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://127.0.0.1:31002/actuator/info/cms/page/get/63a954f14ed12e183cd00892", Map.class);
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://" + ServiceID + "/cms/page/get/63a954f14ed12e183cd00892", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
}

