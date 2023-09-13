package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@RequestMapping("/freemarker")
@Controller          //不能使用@RestController，要输出HTML页面，使用@RestController输出的是JSON数据
public class FreeMarkerController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/course")
    public String course(Map<String,Object> map) {
        //用resttemplate请求轮播图模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseviewmod/402885898a137e2f018a138ef8220000",Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "course";
    }



    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map) {
        //用resttemplate请求轮播图模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f",Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "index_banner";
    }
    //测试1



    @RequestMapping("/test1")
    public String test1(Map<String,Object> map){
        //map就是freemarker模板使用的数据
        map.put("name","test1");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
//向数据模型放数据
        map.put("stus",stus);
//准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
//向数据模型放数据
        map.put("stu1",stu1);
//向数据模型放数据
        map.put("stuMap",stuMap);
//返回模板文件名称
        //返回free marker模板的位置，基于resources/templates路径
        return "test1";
    }

    //测试2
    //测试3
}
