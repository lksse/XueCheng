package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CourseGrade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSysRepository cmsSysRepository;
    @Test
    public void TestFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    //测试分页查询
    @Test
    public void TestFindPage(){
        //分页参数
        int page = 1;  //从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);

        }
    //修改
    @Test
    public void testUpdate(){
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        //Optional:jdk1.8引入的一个容器对象 ，其中isPresent方法判断所包含的对象是否为空

        if (optional.isPresent()){    //如果isPresnt返回的值为false，则所包含的对象为空，否则可以使用get()取出对象进行操作
            CmsPage cmsPage = optional.get();
            //空值判断，使用optional可以规范化           原来的：/*   if (cmsPage != null){}*/
            //设置修改的值
            cmsPage.setPageAliase("Test01");
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }
    //跟据页面名称查询
    @Test
    public void testfindByPageName() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("index.html");
        System.out.println(cmsPage);
    }



//根据条件查询
    @Test
    public void testFindAllByExample(){
        //分页参数
        int page = 0;  //从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        //查询条件值对象
        CmsPage cmsPage = new CmsPage();
  /*      cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板ID条件
        cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");*/

        //设置页面别名查询
        cmsPage.setPageAliase("课程详");
//定义匹配器Example
        //of() 的第二个参数：条件匹配器 ExampleMatcher
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();

        //条件匹配器，给页面别名属性添加     包含匹配方法:contains()                     人话：模糊查询的设置
        //withMatcher会返回一个exampleMatcher对象
        exampleMatcher =  exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());


        //匹配器更好的写法
        ExampleMatcher exampleMatcher1 = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //定义条件对象
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);
    }

    //查询课程等级
    @Test
    public void TsetFindCourseGradeByType(){
        String d_type = "200";
        CourseGrade bydType = cmsSysRepository.findByDType(d_type);
        System.out.println(bydType);
    }




























}
