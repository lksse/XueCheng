package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Test
    public void TestRestTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);

    }

    @Test
    public void TestTemplate(){
        GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("64f89f697473fe39388ae14a")));
        GridFSFile gridfsfile1 = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5ad9a41d68db522910e00846")));


        System.out.println(gridfsfile);
        System.out.println(gridfsfile1.getObjectId());
    }


 }


