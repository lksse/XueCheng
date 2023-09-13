package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class FreeMarkerTest {
    @Autowired
    RestTemplate restTemplate;

    //测试freemarker的静态化，基于ftl文件生成HTML文件
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        //拿类的路径
        String classpath = this.getClass().getResource("/").getPath();
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates"));
        //获取模板路径
        Template template = configuration.getTemplate("test1.ftl");
        //定义数据模型
        Map map = getMap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template ,map);
//        System.out.println(content);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("W:/FreeMarker/test2.html"));
        //输出文件
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    //基于模板的内容（字符串）生成html
    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
            //模板内容
            //模板内容，这里测试时使用简单的字符串作为模板
            String templateString="" +
                "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                " 名称：${name}\n" +
                " </body>\n" +
                "</html>";
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template",templateString);
                //配置中设置模板加载器
                configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        Template template = configuration.getTemplate("template", "utf-8");

        //定义数据模型
        Map<String,Object> map = new HashMap<>();
        map.put("name","hello");

        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template ,map);
//        System.out.println(content);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("W:/FreeMarker/test2.html"));
        //输出文件
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void test2(){

        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f",Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }




    //获取数据模型
    public Map getMap() {
        Map map = new HashMap<>();
        map.put("name", "test1");
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
        map.put("stus", stus);
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        map.put("stu1", stu1);
        map.put("stuMap", stuMap);
        return map;
    }

}
