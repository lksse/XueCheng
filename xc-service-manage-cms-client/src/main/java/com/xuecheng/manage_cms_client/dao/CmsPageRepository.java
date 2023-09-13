package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

// 对于MongoDB的Dao ，继承MongoRepository，其中CmsPage为模型，String为组件的类型
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
                                            //关于实现在，在Test中，MongoRepository有很多可以直接使用的查询方法
         //根据页面名称查询
         CmsPage findByPageName(String pageName);
        //验页面名称，站点ID 页面Path的唯一性
        CmsPage findByPageNameAndSiteIdAndPageWebPath(String PageName, String siteId, String pageWebPath);

//        Optional<CmsPage> findById(String pageId);
}

