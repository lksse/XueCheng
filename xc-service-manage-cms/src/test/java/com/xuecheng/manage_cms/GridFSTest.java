package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFSTest {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    //将一个模板文件存到GridFS中
    @Test
    public void GridFsTemplate() throws FileNotFoundException {
        //定义File
       //File file = new File("B:/1XueChengZaiXian/xcEduService01/xc-service-manage-cms/src/test/resources/templates/index_banner.ftl");
       File file = new File("B:/1XueChengZaiXian/xcEduService01/test-freemarker/src/main/resources/templates/course.ftl");
       //输入流
        InputStream inputStream = new FileInputStream(file);
        //返回一个ID
        ObjectId objectId = gridFsTemplate.store(inputStream, "course.ftl");
        System.out.println(objectId);
        //64f82a897473fe5bac143ed9          课程详情模板ID     课程详情测试模板2
        //64f89f697473fe39388ae14a
        //64f9afe42e1b3c38f40f9c59
        //64f9b4ff2e1b3c7ae8a637ff

    }

    //从Grid FS中取模板文件
    @Test
    public void getGridFsTemplate() throws IOException {
        //根据ID查文件
        GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("6427e91cb99ddb8a8c96a33e")));
//        OutputStream outputStream = new
        //打开GridFsBucket下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridfsfile.getObjectId());
        //创建GridResources对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridfsfile,gridFSDownloadStream);
        //从流中取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(content);

    }



 }


