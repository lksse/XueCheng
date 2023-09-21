package com.xuecheng.search;


import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {
    @Autowired
    RestHighLevelClient restHighLevelClient;


    /**    测试删除索引
     *
     */
    @Test
    public void testDeleteIndex() throws IOException {
        //创建索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //删除索引
       /*DeleteIndexResponse deleteIndexResponse = restHighLevelClient.indices().delete(deleteIndexRequest);
        */
            //操作索引的客户端
            IndicesClient indices = restHighLevelClient.indices();
            //删除上面指定的索引
            DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest);
        //删除后响应的结果
            //拿到响应
            boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**     测试创建索引库
     * number_of_replicas      副本数量
     * number_of_shards       分片数量
     */
    @Test
    public void testCreateIndex() throws IOException {
        //创建索引请求对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
            //对创建的索引对象设置参数
            createIndexRequest.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","0"));
                //指定映射
                createIndexRequest.mapping("doc","{\n" +
                        "\t\"properties\": {\n" +
                        "\t\t\"name\": {\n" +
                        "\t\"type\": \"text\"\n" +
                        "\t},\n" +
                        "\t\"description\": {\n" +
                        "\t\t\"type\": \"text\"\n" +
                        "\t},\n" +
                        "\t\"studymodel\": {\n" +
                        "\t\t\"type\": \"keyword\"\n" +
                        "\t}\n" +
                        "\t}\n" +
                        "}", XContentType.JSON);
        //创建操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行创建索引
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        //拿到响应对象
        boolean acknowledged = createIndexResponse.isAcknowledged();

        System.out.println(acknowledged);
    }

    /**
     * 添加文档
     */
    @Test
    public void testAddDocument() throws IOException {
        //添加的JSON数据
        HashMap<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("name","springCloud实战");
            jsonMap.put("description","Bootstrap是由Twitter推出的一个前台页面开发框架");
            jsonMap.put("studymodel","201002");
        //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        //指定文档内容
        IndexRequest source = indexRequest.source(jsonMap);
        //客户端进行HTTP请求   拿到响应
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
        //响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }
    /**
     * 查询文档
     */
    @Test
    public void testGetDocument() throws IOException {
        //索引请求对象
        GetRequest indexRequest = new GetRequest("xc_course", "doc","VRLLnIoBrB8-xp-foVZu");
        //客户端查询
        GetResponse getResponse= restHighLevelClient.get(indexRequest);
            //得到文档内容
            Map<String, Object> source = getResponse.getSourceAsMap();
        //打印输出
        System.out.println(source);
    }

    /**
     * 更新文档
     */
    @Test
    public void testUpdateDocument() throws IOException {
        //索引请求对象
        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc","VRLLnIoBrB8-xp-foVZu");
            //更新信息
            HashMap<String, Object> map = new HashMap<>();
            map.put("name","updateInfo");
            //存入请求
            updateRequest.doc(map);
        //客户端更新
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
        //获取更新状态
        RestStatus status = updateResponse.status();
        //打印输出
        System.out.println(status);
    }

    /**
     * 删除文档
     */
    @Test
    public void testDeleteDocument() throws IOException {
        //索引请求对象
        DeleteRequest deleteRequest = new DeleteRequest("xc_course", "doc","VRLLnIoBrB8-xp-foVZu");
        //客户端删除信息
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        //获取删除结果
        RestStatus status = deleteResponse.status();
        //打印输出
        System.out.println(status);
    }
}
