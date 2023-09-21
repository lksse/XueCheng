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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    /**
     * 搜索全部记录
     */
    @Test
    public void testSearchAll() throws IOException, ParseException {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
            //搜索对象指定类型
            searchRequest.types("doc");
        //创建搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //搜索源设置搜索方式
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());          //QueryBuilders.matchAllQuery()-----搜索全部
            //设置源文档字段过滤------------第一个结果集包括哪些字段，第二个结果集不包括哪些字段
            searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp"},new String[]{});
        //搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取匹配的搜索结果
            SearchHits hits = searchResponse.getHits();
            //匹配到的总记录数
            long totalHits = hits.getTotalHits();
            //匹配度高的文档
            SearchHit[] searchHits = hits.getHits();

        //日期格式化对象
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //遍历输出
        for (SearchHit hit:searchHits) {
            //文档主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //之前设置了源文档字段过滤集，所以搜索不到
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            //打印
            System.out.println(name);
            System.out.println(description);
            System.out.println(studymodel);
            System.out.println(timestamp);
        }
    }
    /**
     * 分页搜索记录
     */
    @Test
    public void testSearchPage() throws IOException, ParseException {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //搜索对象指定类型
        searchRequest.types("doc");
        //创建搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //页码
            int page = 1;
            //每页记录数
            int size = 1;
            //计算记录起始下标
            int from = (page - 1) * size;
            //设置分页参数
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);
        //搜索源设置搜索方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());          //QueryBuilders.matchAllQuery()-----搜索全部
        //设置源文档字段过滤------------第一个结果集包括哪些字段，第二个结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp"},new String[]{});
        //搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        //获取匹配的搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //匹配度高的文档
        SearchHit[] searchHits = hits.getHits();

        //日期格式化对象
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //遍历输出
        for (SearchHit hit:searchHits) {
            //文档主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //之前设置了源文档字段过滤集，所以搜索不到
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            //打印
            System.out.println(name);
            System.out.println(description);
            System.out.println(studymodel);
            System.out.println(timestamp);
        }
    }
    /**
     * 精确查询和根据ID查询
     */
    @Test
    public void testSearchTermQuery() throws IOException, ParseException {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //搜索对象指定类型
        searchRequest.types("doc");
        //创建搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索源设置搜索方式
                    //termQuery精确查询
                  //searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));          //termQuery-----精确查询
                    // searchSourceBuilder.query(QueryBuilders.termQuery("name","spring开发"));     //因为是整体精确查询，没有相匹配的值，搜不出来
                    //设置源文档字段过滤------------第一个结果集包括哪些字段，第二个结果集不包括哪些字段
                            //根据ID查询
                                //设置主键
                                String[] ids = new String[]{"1","2"};
                                searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));          //注意：这里的QueryBuilders.termsQuery 是termsQuery
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp"},new String[]{});
        //搜索请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        //获取匹配的搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //匹配度高的文档
        SearchHit[] searchHits = hits.getHits();

        //日期格式化对象
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //遍历输出
        for (SearchHit hit:searchHits) {
            //文档主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //之前设置了源文档字段过滤集，所以搜索不到
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            //打印
            System.out.println(name);
            System.out.println(description);
            System.out.println(studymodel);
            System.out.println(timestamp);
        }
    }

}
