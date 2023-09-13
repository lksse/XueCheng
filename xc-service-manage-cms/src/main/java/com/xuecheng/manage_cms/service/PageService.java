package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsPageSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.HashAdapter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsPageSiteRepository cmsPageSiteRepository;
    @Autowired
    CmsConfigRepository cmsConfigRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     *页面查询方法
     * @param page 页码，从第一页开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList( int page,  int size, QueryPageRequest queryPageRequest){
//        cmsPageRepository.findAll();
        if (queryPageRequest == null) {
            QueryResult queryResult = new QueryResult();
        }

        //条件匹配器，给页面别名属性添加     包含匹配方法:contains()
        //withMatcher会返回一个exampleMatcher对象

        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //设置条件值：站点ＩＤ
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板ID
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        //定义条件对象
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        //分页参数
        if (page <=0){
            page = 1;
        }
//        int page = 1;  //从0开始
        page = page - 1;
        if (size <= 0){
            size = 10;
        }
//        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
//        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);    //带匹配器的查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());                      //数据列表
        queryResult.setTotal(all.getTotalElements());               //数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
    //查分类       TODO
    public QueryResponseResult findSiteList(QueryPageRequest queryPageRequest){
        List<CmsSite> all = cmsPageSiteRepository.findAll();
        QueryResult queryResult = new QueryResult();
        System.out.println(all);
//        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,);)

        return null;
    }
    //新增页面
    public CmsPageResult  add(CmsPage cmsPage){
        //校验页面名称，站点ID 页面Path的唯一性
        //根据这3个数据去cmsPage集合，如果查到，说明存在
        CmsPage byPageNameAndSiteIdAndPageWebPath = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(byPageNameAndSiteIdAndPageWebPath != null){
//            return new CmsPageResult(CommonCode.FAIL,null);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);                //！自定义异常
        }
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);

    }
    //根据ID查询页面
    public CmsPage  getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
//        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    //修改页面
    public CmsPageResult update(String id,CmsPage cmsPage){
         //根据ID从数据库查询页面
        CmsPage byId = this.getById(id);
        if (byId != null){
            //设置要修改的数据
            //更新模板id
            byId.setTemplateId(cmsPage.getTemplateId());
        //更新所属站点
            byId.setSiteId(cmsPage.getSiteId());
        //更新页面别名
            byId.setPageAliase(cmsPage.getPageAliase());
         //更新页面名称
            byId.setPageName(cmsPage.getPageName());
         //更新访问路径
            byId.setPageWebPath(cmsPage.getPageWebPath());
         //更新物理路径
            byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
         //更新URL
            byId.setDataUrl(cmsPage.getDataUrl());
        //执行更新
           // CmsPage save = cmsPageRepository.save(byId);
            cmsPageRepository.save(byId);
            //if (save != null) {
         //返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, byId);
                return cmsPageResult;
           // }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //根据ID删除页面
    public ResponseResult delById(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        System.out.println(byId);
        if(byId.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

    //根据id查询cmsConfig
    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }


    //页面静态化
    public String getPageHtml(String pageId){
        /*
        * 1.获取URL
        * 2.根据url获取数据模型
        * 3.
        * 4.静态化
        * */
        //获取数据模型
        Map model = getNodeByPageId(pageId);
        if (model == null){
            ExceptionCast.cast(CmsCode.CMS_PAGEISNULL);
        }
        //获取模板信息
        String templateByPageId = getTemplateByPageId(pageId);
        if (templateByPageId == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        System.out.println(model);
        //执行静态化
        String html = generateHtml(templateByPageId, model);
        return html;
    }


    //执行静态化
    private String generateHtml(String templateContent,Map nodel){
        //配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //配置加载器
        stringTemplateLoader.putTemplate("template",templateContent);
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        try {
            Template template = configuration.getTemplate("template");
            //静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, nodel);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取数据模型
        private Map getNodeByPageId(String pageId){
        //取页面信息dataURL
            CmsPage cmsPage = this.getById(pageId);
            if (cmsPage == null){
                ExceptionCast.cast(CmsCode.CMS_PAGEISNULL);
            }
            //拿URL
            String dataUrl = cmsPage.getDataUrl();
            if (StringUtils.isEmpty(dataUrl)){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
            }
//            通过restTemplate请求dataURL数据
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            Map body = forEntity.getBody();
            return body;
        }
    //获得模板信息
    private String getTemplateByPageId(String pageId){
        //取页面信息dataURL
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGEISNULL);
        }
        //获取页面模板ID
        String templateId = cmsPage.getTemplateId();
        if (templateId == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> byId = cmsTemplateRepository.findById(templateId);
        if (byId.isPresent()){
            CmsTemplate cmsTemplate = byId.get();
            //获取模板文件ID
            String templateFileId= cmsTemplate.getTemplateFileId();
            //从gridFS中取模板文件内容
            //根据ID查文件
            //GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("6427e91cb99ddb8a8c96a33e")));         //轮播图
            //GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("64f89f697473fe39388ae14a")));        //可用，模板报错
            GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("64f9b4ff2e1b3c7ae8a637ff")));
            //GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
//        OutputStream outputStream = new
            //打开GridFsBucket下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridfsfile.getObjectId());
            //创建GridResources对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridfsfile,gridFSDownloadStream);
            //从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();

            }

        }
        return null;
    }

    //页面发布
    public ResponseResult post(String pageId ){
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化的内容存到GridFS
        CmsPage cmsPage = saveHTML(pageId, pageHtml);
        //向MQ发消息
        sendPostpage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //向MQ发送消息
    private void sendPostpage(String pageId){
        //拿页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //拼接消息对象
        Map<String ,String> msg = new HashMap<>();
        msg.put("pageId",pageId);
        //转JSON串
        String jsonString = JSON.toJSONString(msg);
        //站点ID－－－－－－－－－RoutingKey
        String siteId = cmsPage.getSiteId();
        //发送mq消息
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }
    //保存html到GridFS中
    private CmsPage saveHTML(String pageId,String htmlContent){
        //拿页面信息
            CmsPage cmsPage = this.getById(pageId);
            if (cmsPage == null){
                ExceptionCast.cast(CommonCode.INVALID_PARAM);
            }

        ObjectId objectId = null;
        try {
            //将htmlContent内容转成输入流
            InputStream inputStream = IOUtils.toInputStream(htmlContent,"utf-8");
            //将html文件保存到GridFS中
             objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }
        //保存页面
    /*public CmsPageResult save(CmsPage cmsPage) {
        //判断页面是否存在
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            //有这个page，更新page
            System.out.println("gggggggggggggggggIDDDDDDDDD"+cmsPage1.getPageId());
            String tempID = cmsPage1.getPageId();
//           return this.update(cmsPage1.getPageId(),cmsPage);
           return this.update(tempID,cmsPage1);
        }
        return this.add(cmsPage1);

    }*/
        public CmsPageResult save(CmsPage cmsPage) {
            //判断页面是否存在
            CmsPage one = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
            if(one!=null){
                //进行更新
                return this.update(one.getPageId(),cmsPage);
            }
            return this.add(cmsPage);

        }

        //一键发布
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {

        //页面信息存到cmsPage中
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String pageId = save.getCmsPage().getPageId();
        //执行页面发布：1，静态化 2，存Grid FS 3，MQ发送消息
        ResponseResult post = this.post(pageId);
        if (!post.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //拼接URL
        CmsPage cmsPage1 = save.getCmsPage();
        String siteId = cmsPage1.getSiteId();      //拿siteID
        CmsSite cmsSite = this.getCmsSite(siteId);  //拿站点信息
        String pageUrl = cmsSite.getSiteDomain()+cmsSite.getSiteWebPath()+cmsPage1.getPageWebPath()+cmsPage.getPageName();
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);


    }


    //根据站点ID查询站点信息
    public CmsSite getCmsSite(String siteID){
        Optional<CmsSite> byId = cmsPageSiteRepository.findById(siteID);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }
}
